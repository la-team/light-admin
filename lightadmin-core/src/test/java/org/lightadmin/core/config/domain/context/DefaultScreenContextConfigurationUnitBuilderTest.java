package org.lightadmin.core.config.domain.context;

import org.junit.Test;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

import static org.junit.Assert.assertEquals;

public class DefaultScreenContextConfigurationUnitBuilderTest {

	@Test
	public void defaultConfigurationUnitCreatedForDomainType() throws Exception {
		ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().build();

		assertEquals( DomainConfigurationUnitType.SCREEN_CONTEXT, configurationUnit.getDomainConfigurationUnitType() );
		assertEquals( DomainType.class, configurationUnit.getDomainType() );
	}

	private DefaultScreenContextConfigurationUnitBuilder screenContextBuilder() {
		return new DefaultScreenContextConfigurationUnitBuilder( DomainType.class );
	}

	@Test
	public void defaultConfigurationUnitWithUndefinedScreenNameCreated() throws Exception {
		ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().build();

		assertEquals( "DomainType", configurationUnit.getScreenName() );
	}

	@Test
	public void defaultConfigurationUnitWithUndefinedMenuItemNameCreated() throws Exception {
		ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().build();

		assertEquals( "DomainType", configurationUnit.getMenuItemName() );
	}

	@Test
	public void configurationWithScreenNameDefined() throws Exception {
		ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().screenName( "Test Screen Name" ).build();

		assertEquals( "Test Screen Name", configurationUnit.getScreenName() );
	}

	@Test
	public void configurationWithMenuItemNameDefined() throws Exception {
		ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().menuName( "Test Menu Name" ).build();

		assertEquals( "Test Menu Name", configurationUnit.getMenuItemName() );
	}

	@Test
	public void configurationFullPacked() throws Exception {
		ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().screenName( "Test Screen Name" ).menuName( "Test Menu Name" ).build();

		assertEquals( "Test Screen Name", configurationUnit.getScreenName() );
		assertEquals( "Test Menu Name", configurationUnit.getMenuItemName() );
	}

	private static class DomainType {

	}
}