package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.junit.Test;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DomainConfigurationUnitTypeTest {

	@Test( expected = IllegalArgumentException.class )
	public void undefinedConfigurationUnit() {
		DomainConfigurationUnitType.forName( "Undefined configuration unit" );
	}

	@Test
	public void findConfigurationUnitByName() throws Exception {
		assertEquals( DomainConfigurationUnitType.SCREEN_CONTEXT, DomainConfigurationUnitType.forName( "screenContext" ) );
	}

	@Test
	public void definedUnitTypeNames() throws Exception {
		final String[] expectedUnitTypeNames = new String[] {
			"screenContext", "configuration", "listView", "scopes", "filters", "showView"
		};

		assertArrayEquals( expectedUnitTypeNames, unitTypeNames( DomainConfigurationUnitType.values() ) );
	}

	private String[] unitTypeNames( DomainConfigurationUnitType[] unitTypes ) {
		final Set<String> unitTypeNames = newLinkedHashSet();
		for ( DomainConfigurationUnitType unitType : unitTypes ) {
			unitTypeNames.add( unitType.getName() );
		}
		return unitTypeNames.toArray( new String[unitTypeNames.size()] );
	}
}