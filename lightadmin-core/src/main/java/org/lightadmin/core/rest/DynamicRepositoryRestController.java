package org.lightadmin.core.rest;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfigurationAware;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.config.domain.scope.ScopeMetadataUtils;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.search.SpecificationCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.rest.webmvc.PagingAndSorting;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;

@SuppressWarnings( "unchecked" )
@RequestMapping( "/rest" )
public class DynamicRepositoryRestController extends RepositoryRestController implements GlobalAdministrationConfigurationAware {

	private final SpecificationCreator specificationCreator = new SpecificationCreator();

	private GlobalAdministrationConfiguration configuration;

	@ResponseBody
	@RequestMapping( value = "/{repositoryName}/scope/{scopeName}/search", method = RequestMethod.GET )
	public ResponseEntity<?> filterEntities( ServletServerHttpRequest request, @SuppressWarnings( "unused" ) URI baseUri, PagingAndSorting pageSort, @PathVariable String repositoryName, @PathVariable String scopeName ) throws IOException {

		final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName( repositoryName );

		final DomainTypeEntityMetadata domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
		final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

		final ScopeMetadata scope = domainTypeAdministrationConfiguration.getScopes().getScope( scopeName );

		final Specification filterSpecification = specificationFromRequest( request, domainTypeEntityMetadata );

		if ( isPredicateScope( scope ) ) {
			final ScopeMetadataUtils.PredicateScopeMetadata predicateScope = ( ScopeMetadataUtils.PredicateScopeMetadata ) scope;

			final Page page = findBySpecificationAndPredicate( repository, filterSpecification, predicateScope.predicate(), pageSort );

			return negotiateResponse( request, page, pageMetadata( page ) );
		}

		if ( isSpecificationScope( scope ) ) {
			final Specification scopeSpecification = ( ( ScopeMetadataUtils.SpecificationScopeMetadata ) scope ).specification();

			Page page = findItemsBySpecification( repository, and( scopeSpecification, filterSpecification ), pageSort );

			return negotiateResponse( request, page, pageMetadata( page ) );
		}

		Page page = findItemsBySpecification( repository, filterSpecification, pageSort );

		return negotiateResponse( request, page, pageMetadata( page ) );
	}

	private Page findBySpecificationAndPredicate( DynamicJpaRepository repository, final Specification specification, Predicate predicate, final PagingAndSorting pageSort ) {
		final List<?> items = findItemsBySpecification( repository, specification, pageSort.getSort() );

		return selectPage( newArrayList( Collections2.filter( items, predicate ) ), pageSort );
	}

	private Page<?> findItemsBySpecification( final DynamicJpaRepository repository, final Specification specification, final PagingAndSorting pageSort ) {
		return repository.findAll( specification, pageSort );
	}

	private List<?> findItemsBySpecification( final DynamicJpaRepository repository, final Specification specification, final Sort sort ) {
		return repository.findAll( specification, sort );
	}

	private Page<?> selectPage( List<Object> items, PagingAndSorting pageSort ) {
		final List<Object> itemsOnPage = items.subList( pageSort.getOffset(), Math.min( items.size(), pageSort.getOffset() + pageSort.getPageSize() ) );

		return new PageImpl<Object>( itemsOnPage, pageSort, items.size() );
	}

	private boolean isSpecificationScope( final ScopeMetadata scope ) {
		return scope instanceof ScopeMetadataUtils.SpecificationScopeMetadata;
	}

	private boolean isPredicateScope( final ScopeMetadata scope ) {
		return scope instanceof ScopeMetadataUtils.PredicateScopeMetadata;
	}

	private Specification and( Specification specification, Specification otherSpecification ) {
		return Specifications.where( specification ).and( otherSpecification );
	}

	private Specification specificationFromRequest( ServletServerHttpRequest request, final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> entityMetadata ) {
		final Map<String, String[]> parameters = request.getServletRequest().getParameterMap();

		return specificationCreator.toSpecification( entityMetadata, parameters );
	}

	private ResponseEntity<?> negotiateResponse( ServletServerHttpRequest request, Page page, PagedResources.PageMetadata pageMetadata ) throws IOException {
		Method negotiateResponseMethod = ReflectionUtils.findMethod( getClass(), "negotiateResponse", ServletServerHttpRequest.class, HttpStatus.class, HttpHeaders.class, Object.class );

		ReflectionUtils.makeAccessible( negotiateResponseMethod );

		try {
			return ( ResponseEntity<?> ) negotiateResponseMethod.invoke( this, request, HttpStatus.OK, new HttpHeaders(), new PagedResources( toResources( page ), pageMetadata, Lists.<Link>newArrayList() ) );
		} catch ( InvocationTargetException ex ) {
			ReflectionUtils.rethrowRuntimeException( ex.getTargetException() );
			return null; // :)
		} catch ( IllegalAccessException ex ) {
			throw new UndeclaredThrowableException( ex );
		}
	}

	private PagedResources.PageMetadata pageMetadata( final Page page ) {
		return new PagedResources.PageMetadata( page.getSize(), page.getNumber() + 1, page.getTotalElements(), page.getTotalPages() );
	}

	private List<Object> toResources( Page page ) {
		if ( !page.hasContent() ) {
			return newLinkedList();
		}

		List<Object> allResources = newArrayList();
		for ( final Object item : page ) {
			allResources.add( item );
		}
		return allResources;
	}

	@Override
	@Autowired
	public void setGlobalAdministrationConfiguration( final GlobalAdministrationConfiguration configuration ) {
		this.configuration = configuration;
	}
}