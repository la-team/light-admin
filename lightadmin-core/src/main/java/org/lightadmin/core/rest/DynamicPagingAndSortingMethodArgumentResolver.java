package org.lightadmin.core.rest;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.PagingAndSorting;
import org.springframework.data.rest.webmvc.PagingAndSortingMethodArgumentResolver;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DynamicPagingAndSortingMethodArgumentResolver extends PagingAndSortingMethodArgumentResolver {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Autowired
	private RepositoryRestConfiguration repositoryRestConfiguration;

	@Override
	public Object resolveArgument( final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory ) throws Exception {
		final PagingAndSorting pagingAndSorting = ( PagingAndSorting ) super.resolveArgument( parameter, mavContainer, webRequest, binderFactory );

		final DomainTypeAdministrationConfiguration configuration = resolveDomainTypConfiguration( webRequest );

		final Sort sort = resolveSortFields( pagingAndSorting.getSort(), configuration );

		return new PagingAndSorting( repositoryRestConfiguration, newPageRequest( pagingAndSorting, sort ) );
	}

	private PageRequest newPageRequest( final PagingAndSorting pagingAndSorting, final Sort sort ) {
		return new PageRequest( pagingAndSorting.getPageNumber(), pagingAndSorting.getPageSize(), sort );
	}

	private DomainTypeAdministrationConfiguration resolveDomainTypConfiguration( NativeWebRequest webRequest ) {
		final String requestURI = ( ( HttpServletRequest ) webRequest.getNativeRequest() ).getRequestURI();

		for ( DomainTypeAdministrationConfiguration configuration : globalAdministrationConfiguration.getDomainTypeConfigurations().values() ) {
			if ( requestURI.contains( "/rest/" + configuration.getDomainTypeName() ) ) {
				return configuration;
			}
		}
		throw new RuntimeException( "Searching for unknown domain type repository" );
	}

	private Sort resolveSortFields( Sort sort, final DomainTypeAdministrationConfiguration configuration ) {
		final List<Sort.Order> orders = newLinkedList();
		for ( Sort.Order order : sort ) {
			final String fieldName = resolveField( order.getProperty(), configuration );
			orders.add( new Sort.Order( order.getDirection(), fieldName ) );
		}
		return new Sort( orders );
	}

	private String resolveField( String fieldMetadataUUID, DomainTypeAdministrationConfiguration configuration ) {
		for ( FieldMetadata field : configuration.getListViewFragment().getFields() ) {
			if ( field.isSortable() && field.getUuid().equals( fieldMetadataUUID ) ) {
				return ( ( PersistentFieldMetadata ) field ).getField();
			}
		}
		throw new RuntimeException( "Trying to sort on unknown field." );
	}
}