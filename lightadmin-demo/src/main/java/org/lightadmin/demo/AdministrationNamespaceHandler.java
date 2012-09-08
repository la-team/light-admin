package org.lightadmin.demo;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class AdministrationNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser( "administration", new AdministrationConfigBeanDefinitionParser() );
	}
}