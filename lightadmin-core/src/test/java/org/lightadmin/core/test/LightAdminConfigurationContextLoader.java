package org.lightadmin.core.test;

import org.lightadmin.core.util.LightAdminConfigurationUtils;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

public class LightAdminConfigurationContextLoader extends AnnotationConfigContextLoader {

	private static final String CONFIGURATIONS_BASE_PACKAGE = "org.lightadmin.core.test.config";

	@Override
	protected void customizeContext( final GenericApplicationContext context ) {
		MockPropertySource mockPropertySource = new MockPropertySource();
		mockPropertySource.setProperty( LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_PACKAGE, CONFIGURATIONS_BASE_PACKAGE );
		context.getEnvironment().getPropertySources().addFirst( mockPropertySource );
	}
}