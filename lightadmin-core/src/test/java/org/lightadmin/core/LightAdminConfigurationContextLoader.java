package org.lightadmin.core;

import org.lightadmin.core.config.support.AdministrationConfigBeanRegistryPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

public class LightAdminConfigurationContextLoader extends AnnotationConfigContextLoader  {

	@Override
	protected void customizeContext( final GenericApplicationContext context ) {
		final AdministrationConfigBeanRegistryPostProcessor postProcessor = new AdministrationConfigBeanRegistryPostProcessor( "org.lightadmin.core.config" );
		postProcessor.postProcessBeanDefinitionRegistry( context );
	}
}