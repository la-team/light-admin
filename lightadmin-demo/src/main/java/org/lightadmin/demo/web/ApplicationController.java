package org.lightadmin.demo.web;

import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.repository.RepositoryMetadata;
import org.springframework.data.rest.repository.invoke.CrudMethod;
import org.springframework.data.rest.repository.invoke.RepositoryMethodResponse;
import org.springframework.data.rest.webmvc.PagingAndSorting;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Controller
public class ApplicationController {

	@Autowired
	private ApplicationContext applicationContext;

	@ExceptionHandler( Exception.class )
	public String handleException() {
		return "redirect:/";
	}

	@RequestMapping( value = "/", method = RequestMethod.GET)
	public String dashboard() {
		return "dashboard";
	}

	@RequestMapping( value = "/domain/{domainType}", method = RequestMethod.GET )
	public String list(@PathVariable String domainType, Model model ) {
		final DynamicJpaRepository repository = repositoryFor( domainType );

		final List entries = repository.findAll();

		model.addAttribute( "entries", entries );
		model.addAttribute( "domainType", domainType );

		return "index";
	}

	private DynamicJpaRepository repositoryFor( String domainType ) {
		return  ( DynamicJpaRepository ) applicationContext.getBean( domainType + "Repository" );
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
