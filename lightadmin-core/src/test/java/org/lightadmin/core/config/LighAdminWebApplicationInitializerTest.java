package org.lightadmin.core.config;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.util.LightAdminConfigurationUtils;

import javax.servlet.ServletContext;

public class LighAdminWebApplicationInitializerTest {

	private LighAdminWebApplicationInitializer testee;

	@Before
	public void setUp() throws Exception {
		this.testee = new LighAdminWebApplicationInitializer();
	}

	@Test
	public void lightAdminModuleNotConfiguredProperly() throws Exception {
		final ServletContext servletContext = EasyMock.createMock( ServletContext.class );
		EasyMock.expect( servletContext.getInitParameter( LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_PACKAGE )).andReturn( null ).anyTimes();
		EasyMock.expect( servletContext.getInitParameter( LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_URL )).andReturn( null ).anyTimes();

		servletContext.log( "LightAdmin Web Administration Module is disabled by default. Skipping." );
		EasyMock.expectLastCall().once();

		EasyMock.replay( servletContext );

		testee.onStartup( servletContext );

		EasyMock.verify( servletContext );
	}
}