package org.lightadmin.demo.web;

import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Controller
public class ApplicationController {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ApplicationContext applicationContext;

//	@Autowired
//	@Qualifier("productRepository")
	private DynamicJpaRepository productRepository;

	@Autowired
	private Validator validator;

	@PostConstruct
	public void postConstruct() {
		productRepository = ( DynamicJpaRepository ) applicationContext.getBean( "productRepository" );
	}

	@ExceptionHandler( Exception.class )
	public String handleException() {
		return "redirect:/";
	}

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( validator );
	}

	@ModelAttribute( "menuItems" )
	public Set<String> menuItems() {
		return newHashSet( "Menu Item 1", "Menu Item 2", "Menu Item 3" );
	}

	@RequestMapping( "/" )
	public String index( Model model ) {
		Product product = new Product( "Box", BigDecimal.TEN );
		Errors errors = new BeanPropertyBindingResult( product, "product" );
		validator.validate( product, errors );

		model.addAttribute( "entries", loadEntries() );
		return "index";
	}

	private List<Product> loadEntries() {
		return productRepository.findAll( entryNameEqHello() );
	}

	public static Specification<Product> entryNameEqHello() {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate( final Root<Product> root, final CriteriaQuery<?> query, final CriteriaBuilder cb ) {
				return cb.equal( root.get( "name" ), "Box" );
			}
		};
	}
}
