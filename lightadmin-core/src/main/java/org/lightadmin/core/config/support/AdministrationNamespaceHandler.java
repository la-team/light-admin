package org.lightadmin.core.config.support;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class AdministrationNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser( "administration", new AdministrationConfigBeanDefinitionParser() );
	}
}