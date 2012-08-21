package org.lightadmin.demo.web;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.lightadmin.demo.model.Entry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.google.common.collect.Sets.newHashSet;

@Controller
public class ApplicationController {

	@PersistenceContext
	private EntityManager em;

	@ExceptionHandler( Exception.class )
	public String handleException() {
		return "redirect:/";
	}

	@ModelAttribute("menuItems")
	public Set<String> menuItems() {
		return newHashSet("Menu Item 1", "Menu Item 2", "Menu Item 3");
	}

	@RequestMapping( "/" )
	public String index( Model model ) {
		model.addAttribute( "entries", loadEntries() );
		return "index";
	}

	private List<Entry> loadEntries() {
		return em.createQuery( "select e from Entry e", Entry.class ).getResultList();
	}
}