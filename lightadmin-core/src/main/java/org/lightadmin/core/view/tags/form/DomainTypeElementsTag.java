package org.lightadmin.core.view.tags.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.web.ApplicationController;
import org.springframework.util.Assert;

public class DomainTypeElementsTag extends SimpleTagSupport {

	private Class<?> domainType;

	private String idVar;
	private String stringRepresentationVar;

	@Override
	public void doTag() throws JspException, IOException {

		JspContext jspContext = getJspContext();
		JspFragment tagBody = getJspBody();

		GlobalAdministrationConfiguration configuration =
				(GlobalAdministrationConfiguration) jspContext.findAttribute(ApplicationController.ADMINISTRATION_CONFIGURATION_KEY);

		Assert.notNull(configuration, "<domainTypeAdministrationConfiguration> is not found in the JspContext");

		DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forDomainType(domainType);

		if (domainTypeConfiguration == null) {
			jspContext.setAttribute(idVar, "");
			jspContext.setAttribute(stringRepresentationVar, "Not Implemented");
			tagBody.invoke(null);
			return;
		}

		// TODO: Implement configurable ordering
		List allElements = domainTypeConfiguration.getRepository().findAll();
		allElements = sortByNaturalOrder(allElements, domainTypeConfiguration);


		DomainTypeAttributeMetadata idAttribute = domainTypeConfiguration.getDomainTypeEntityMetadata().getIdAttribute();
		EntityNameExtractor<Object> nameExtractor = domainTypeConfiguration.getEntityConfiguration().getNameExtractor();
		for (Object element : allElements) {
			jspContext.setAttribute(idVar, idAttribute.getValue(element));
			jspContext.setAttribute(stringRepresentationVar, nameExtractor.apply(element));
			tagBody.invoke(null);
		}
	}

	private List sortByNaturalOrder(List elements, DomainTypeAdministrationConfiguration elementTypeConfiguration) {

		EntityNameExtractor<Object> nameExtractor = elementTypeConfiguration.getEntityConfiguration().getNameExtractor();

		List<Pair<Object, String>> elementNamePairs = new ArrayList<Pair<Object,String>>(elements.size());
		for (Object element : elements) {
			String name = nameExtractor.apply(element);
			elementNamePairs.add(new Pair<Object, String>(element, name));
		}

		Collections.sort(elementNamePairs, new Comparator<Pair<Object, String>>() {
			@Override
			public int compare(Pair<Object, String> pair1, Pair<Object, String> pair2) {
				return pair1.getSecond().compareToIgnoreCase(pair2.getSecond());
			}
		});

		List sortedElements = new ArrayList<Object>(elementNamePairs.size());
		for (Pair<Object, String> elementNamePair : elementNamePairs) {
			sortedElements.add(elementNamePair.getFirst());
		}
		return sortedElements;
	}

	public void setDomainType(Class<?> type) {
		this.domainType = type;
	}

	public void setIdVar(String idVar) {
		this.idVar = idVar;
	}

	public void setStringRepresentationVar(String stringRepresentationVar) {
		this.stringRepresentationVar = stringRepresentationVar;
	}

}
