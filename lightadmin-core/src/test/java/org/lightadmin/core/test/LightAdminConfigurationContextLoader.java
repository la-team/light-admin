package org.lightadmin.core.test;

import org.lightadmin.core.config.beans.AdministrationConfigBeanRegistryPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

public class LightAdminConfigurationContextLoader extends AnnotationConfigContextLoader  {

	private static final String CONFIGURATIONS_BASE_PACKAGE = "org.lightadmin.core.test.config";

	@Override
	protected void customizeContext( final GenericApplicationContext context ) {
		final AdministrationConfigBeanRegistryPostProcessor postProcessor = new AdministrationConfigBeanRegistryPostProcessor( CONFIGURATIONS_BASE_PACKAGE );
		postProcessor.postProcessBeanDefinitionRegistry( context );
	}
}