package org.lightadmin.core.rest;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfigurationAware;
import org.lightadmin.core.config.domain.scope.Scope;
import org.lightadmin.core.config.domain.scope.ScopeUtils;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.search.SpecificationCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.PagingAndSorting;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;

public class DynamicRepositoryRestController extends RepositoryRestController implements GlobalAdministrationConfigurationAware {

	private GlobalAdministrationConfiguration configuration;

	private RestResponseNegotiator responseNegotiator;

	private SpecificationCreator specificationCreator;

	@PostConstruct
	public void init() {
		responseNegotiator = new RestResponseNegotiator( getRepositoryRestConfig(), getHttpMessageConverters(), getMappingHttpMessageConverter() );
		specificationCreator = new SpecificationCreator();
	}

	@ResponseBody
	@RequestMapping( value = "/{repositoryName}/scope/{scopeName}", method = RequestMethod.GET )
	public ResponseEntity<?> scopeEntities( ServletServerHttpRequest request, PagingAndSorting pageSort, @PathVariable String repositoryName, @PathVariable String scopeName ) throws IOException {
		final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName( repositoryName );

		final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

		final Scope scope = domainTypeAdministrationConfiguration.getScopes().getScope( scopeName );

		Page page = findItemsPage( repository, scope, pageSort );

		return negotiateResponse( request, page, pageMetadata( page ) );
	}

	@ResponseBody
	@RequestMapping( value = "/{repositoryName}/filter", method = RequestMethod.GET )
	public ResponseEntity<?> filterEntities( ServletServerHttpRequest request, PagingAndSorting pageSort, @PathVariable String repositoryName ) throws IOException {
		final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName( repositoryName );

		final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

		final Map<String, String[]> parameters = request.getServletRequest().getParameterMap();

		final Specification specification = specificationCreator.toSpecification( domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata(), parameters );

		Page page = findItemsBySpecification( repository, specification, pageSort );

		return negotiateResponse( request, page, pageMetadata( page ) );
	}

	@SuppressWarnings( {"unchecked"} )
	private ResponseEntity<?> negotiateResponse( ServletServerHttpRequest request, Page page, PagedResources.PageMetadata pageMetadata ) throws IOException {
		return responseNegotiator.negotiateResponse( request, HttpStatus.OK, new HttpHeaders(), new PagedResources( toResources( page ), pageMetadata, Lists.<Link>newArrayList() ) );
	}

	@SuppressWarnings( "unchecked" )
	private Page<?> findItemsPage( DynamicJpaRepository repository, Scope scope, PagingAndSorting pageSort ) {
		if ( scope instanceof ScopeUtils.DefaultScope ) {
			return repository.findAll( pageSort );
		}

		if ( scope instanceof ScopeUtils.PredicateScope ) {
			return findItemsByPredicate( repository, ( ( ScopeUtils.PredicateScope ) scope ).predicate(), pageSort );
		}

		return findItemsBySpecification( repository, ( ( ScopeUtils.SpecificationScope ) scope ).specification(), pageSort );
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

	@SuppressWarnings( "unchecked" )
	private Page<?> findItemsBySpecification( final DynamicJpaRepository repository, final Specification specification, final PagingAndSorting pageSort ) {
		return repository.findAll( specification, pageSort );
	}

	@SuppressWarnings( "unchecked" )
	private Page<?> findItemsByPredicate( final DynamicJpaRepository repository, final Predicate predicate, final PagingAndSorting pageSort ) {
		final List<Object> items = repository.findAll( pageSort ).getContent();

		final List<Object> filteredItems = newArrayList( Collections2.filter( items, predicate ) );

		return new PageImpl<Object>( filteredItems, pageSort, items.size() );
	}

	@Override
	@Autowired
	public void setGlobalAdministrationConfiguration( final GlobalAdministrationConfiguration configuration ) {
		this.configuration = configuration;
	}
}