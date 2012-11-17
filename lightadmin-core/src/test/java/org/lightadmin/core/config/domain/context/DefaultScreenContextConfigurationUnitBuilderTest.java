package org.lightadmin.core.config.domain.context;

import org.junit.Test;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

import static org.junit.Assert.assertEquals;

public class DefaultScreenContextConfigurationUnitBuilderTest {

	private DefaultScreenContextConfigurationUnitBuilder testee;

	@Test
	public void defaultConfigurationUnitCreatedForDomainType() throws Exception {
		testee = new DefaultScreenContextConfigurationUnitBuilder( DomainType.class );

		ScreenContextConfigurationUnit configurationUnit = testee.build();

		assertEquals( DomainConfigurationUnitType.SCREEN_CONTEXT, configurationUnit.getDomainConfigurationUnitType() );
		assertEquals( DomainType.class, configurationUnit.getDomainType() );
	}

	@Test
	public void defaultConfigurationUnitWithUndefinedScreenNameCreated() throws Exception {
		testee = new DefaultScreenContextConfigurationUnitBuilder( DomainType.class );

		ScreenContextConfigurationUnit configurationUnit = testee.build();

		assertEquals( "Undefined", configurationUnit.getScreenName() );
	}

	@Test
	public void defaultConfigurationUnitWithUndefinedMenuItemNameCreated() throws Exception {
		testee = new DefaultScreenContextConfigurationUnitBuilder( DomainType.class );

		ScreenContextConfigurationUnit configurationUnit = testee.build();

		assertEquals( "Undefined", configurationUnit.getMenuItemName() );
	}

	@Test
	public void configurationWithScreenNameDefined() throws Exception {
		testee = new DefaultScreenContextConfigurationUnitBuilder( DomainType.class );
		testee.screenName( "Test Screen Name" );

		ScreenContextConfigurationUnit configurationUnit = testee.build();

		assertEquals( "Test Screen Name", configurationUnit.getScreenName() );
	}

	@Test
	public void configurationWithMenuItemNameDefined() throws Exception {
		testee = new DefaultScreenContextConfigurationUnitBuilder( DomainType.class );
		testee.menuName( "Test Menu Name" );

		ScreenContextConfigurationUnit configurationUnit = testee.build();

		assertEquals( "Test Menu Name", configurationUnit.getMenuItemName() );
	}

	@Test
	public void configurationFullPacked() throws Exception {
		testee = new DefaultScreenContextConfigurationUnitBuilder( DomainType.class );
		testee.screenName( "Test Screen Name" ).menuName( "Test Menu Name" );

		ScreenContextConfigurationUnit configurationUnit = testee.build();

		assertEquals( "Test Screen Name", configurationUnit.getScreenName() );
		assertEquals( "Test Menu Name", configurationUnit.getMenuItemName() );
	}

	private static class DomainType {
	}
}