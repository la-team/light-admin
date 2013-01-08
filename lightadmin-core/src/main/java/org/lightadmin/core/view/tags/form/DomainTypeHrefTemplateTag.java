package org.lightadmin.core.view.tags.form;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.rest.DomainTypeResourceSupport;
import org.lightadmin.core.view.tags.AbstractAutowiredTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;

import javax.servlet.jsp.JspException;
import java.io.IOException;

public class DomainTypeHrefTemplateTag extends AbstractAutowiredTag {

	@Autowired(required = true)
	private DomainTypeResourceSupport support;

	@Autowired(required = true)
	private GlobalAdministrationConfiguration globalConfiguration;

	private Class<?> domainType;

	private String idPlaceholder = "{0}";

	@Override
	public void doTag() throws JspException, IOException {
		DomainTypeBasicConfiguration configuration = globalConfiguration.forAssociation(domainType);
		Link selfLinkTemplate = support.selfLink(configuration, idPlaceholder);
		getJspContext().getOut().print(selfLinkTemplate.getHref());
	}

	public void setDomainType(Class<?> domainType) {
		this.domainType = domainType;
	}

	public void setIdPlaceholder(String idPlaceholder) {
		this.idPlaceholder = idPlaceholder;
	}

}
