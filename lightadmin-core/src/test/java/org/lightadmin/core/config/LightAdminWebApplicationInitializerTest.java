package org.lightadmin.core.config;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;

import static org.easymock.EasyMock.*;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_PACKAGE;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_URL;

public class LightAdminWebApplicationInitializerTest {

	private LightAdminWebApplicationInitializer testee;

	@Before
	public void setUp() throws Exception {
		this.testee = new LightAdminWebApplicationInitializer();
	}

	@Test
	public void lightAdminModuleNotConfiguredProperly() throws Exception {
		final ServletContext servletContext = EasyMock.createMock( ServletContext.class );
		expect( servletContext.getInitParameter( LIGHT_ADMINISTRATION_BASE_PACKAGE ) ).andReturn( null ).anyTimes();
		expect( servletContext.getInitParameter( LIGHT_ADMINISTRATION_BASE_URL ) ).andReturn( null ).anyTimes();

		servletContext.log( "LightAdmin Web Administration Module is disabled by default. Skipping." );
		expectLastCall().once();

		replay( servletContext );

		testee.onStartup( servletContext );

		verify( servletContext );
	}
}