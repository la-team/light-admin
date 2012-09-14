package org.lightadmin.demo.web;

import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.repository.DynamicJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ApplicationController {

	@Autowired
	private GlobalAdministrationConfiguration configuration;

	@ExceptionHandler( Exception.class )
	public String handleException() {
		return "redirect:/";
	}

	@RequestMapping( value = "/", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboardView";
	}

	@RequestMapping( value = "/domain/{domainType}", method = RequestMethod.GET )
	public String list(@PathVariable String domainType, Model model ) {
		final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName( domainType );
		final DynamicJpaRepository repository = domainTypeAdministrationConfiguration.getRepository();

		final List entries = repository.findAll();

		model.addAttribute( "entries", entries );
		model.addAttribute( "domainType", domainTypeAdministrationConfiguration.getDomainType() );

		return "listView";
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
