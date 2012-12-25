package org.lightadmin.core.view.tags;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.lightadmin.core.web.ApplicationController;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public abstract class AbstractAutowiredTag extends SimpleTagSupport {

	@Override
	public void setJspContext(JspContext context) {
		super.setJspContext(context);

		AutowireCapableBeanFactory beanFactory =
				(AutowireCapableBeanFactory) context.findAttribute(ApplicationController.BEAN_FACTORY_KEY);
		beanFactory.autowireBean(this);
	}

}
