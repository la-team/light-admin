package org.lightadmin.core.web;

import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.repository.DynamicJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.repository.EntityMetadata;
import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Controller
public class ApplicationController {

	private static final Logger LOG = LoggerFactory.getLogger( ApplicationController.class );

	@Autowired
	private GlobalAdministrationConfiguration configuration;

	@ExceptionHandler( Exception.class )
	public String handleException() {
		return "redirect:/";
	}

	@RequestMapping( value = "/", method = RequestMethod.GET )
	public String dashboard() {
		return "dashboardView";
	}

	@SuppressWarnings( "unchecked" )
	@RequestMapping( value = "/domain/{domainType}", method = RequestMethod.GET )
	public String list( @PathVariable String domainType, Model model ) {
		LOG.info( "Handling request" );

		model.addAttribute( "domainType", configuration.forEntityName( domainType ).getDomainType() );

		return "listView";
	}

	@SuppressWarnings( "unchecked" )
	@RequestMapping( value = "/domain/{domainType}/{entityId}", method = RequestMethod.GET )
	public String show( @PathVariable String domainType, @PathVariable long entityId, Model model  ) {
		final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName( domainType );

		final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

		final Object entity = repository.findOne( entityId );

		model.addAttribute( "domainType", configuration.forEntityName( domainType ).getDomainType() );

		model.addAttribute( "entityDTO", entityDto( entity, domainTypeAdministrationConfiguration.getEntityMetadata() ) );
		model.addAttribute( "entity", entity );

		return "showView";
	}

	@SuppressWarnings( "unchecked" )
	@RequestMapping( value = "/domain/{domainType}/{entityId}/edit", method = RequestMethod.GET )
	public String edit( @PathVariable String domainType, @PathVariable long entityId, Model model  ) {
		final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName( domainType );

		final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

		final Object entity = repository.findOne( entityId );

		model.addAttribute( "domainType", configuration.forEntityName( domainType ).getDomainType() );

		model.addAttribute( "entityDTO", entityDto( entity, domainTypeAdministrationConfiguration.getEntityMetadata() ) );
		model.addAttribute( "entity", entity );

		return "editView";
	}

	private Map<String, Object> entityDto( final Object entity, final EntityMetadata<JpaAttributeMetadata> entityMetadata ) {
		final Map<String, Object> result = newHashMap();
		for ( Map.Entry<String, JpaAttributeMetadata> attrMeta : entityMetadata.embeddedAttributes().entrySet() ) {
			String name = attrMeta.getKey();
			Object val = attrMeta.getValue().get( entity );
			if ( null != val ) {
				result.put( name, val );
			}
		}
		return result;
	}

	//	private List<Product> loadEntries() {
	//		return productRepository.findAll( entryNameEqHello() );
	//	}
	//
	//	public static Specification<Product> entryNameEqHello() {
	//		return new Specification<Product>() {
	//			@Override
	//			public Predicate toPredicate( final Root<Product> root, final CriteriaQuery<?> query, final CriteriaBuilder cb ) {
	//				return cb.equal( root.get( "name" ), "Box" );
	//			}
	//		};
	//	}
}