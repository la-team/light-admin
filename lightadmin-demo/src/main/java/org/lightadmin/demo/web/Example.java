package org.lightadmin.demo.web;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.lightadmin.demo.model.Entry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Example {
	
	@PersistenceContext
	private EntityManager em;

	@RequestMapping("/welcome.html")
	public ModelAndView welcome() {
		List<Entry> entries = em.createQuery("select e from Entry e", Entry.class).getResultList();
		return new ModelAndView("/WEB-INF/pages/example.jsp").addObject("entries", entries);
	}
}
