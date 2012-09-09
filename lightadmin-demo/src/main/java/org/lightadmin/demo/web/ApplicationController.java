package org.lightadmin.demo.web;

import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.demo.model.Entry;
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

@Controller
public class ApplicationController {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ApplicationContext applicationContext;

//	@Autowired
//	@Qualifier("entryRepository")
	private DynamicJpaRepository entryRepository;

	@Autowired
	private Validator validator;

	@PostConstruct
	public void postConstruct() {
		entryRepository = ( DynamicJpaRepository ) applicationContext.getBean( "entryRepository" );
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
		Entry entry = new Entry();
		Errors errors = new BeanPropertyBindingResult( entry, "entry" );
		validator.validate( entry, errors );

		model.addAttribute( "entries", loadEntries() );
		return "index";
	}

	private List<Entry> loadEntries() {
		return entryRepository.findAll( entryNameEqHello() );
	}

	public static Specification<Entry> entryNameEqHello() {
		return new Specification<Entry>() {
			@Override
			public Predicate toPredicate( final Root<Entry> root, final CriteriaQuery<?> query, final CriteriaBuilder cb ) {
				return cb.equal( root.get( "name" ), "Hello!" );
			}
		};
	}
}
