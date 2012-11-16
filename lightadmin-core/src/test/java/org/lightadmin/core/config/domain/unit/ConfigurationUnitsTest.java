package org.lightadmin.core.config.domain.unit;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.junit.Assert.*;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.*;

public class ConfigurationUnitsTest {

	@Test( expected = IllegalArgumentException.class )
	public void nullDomainTypeNotAllowed() throws Exception {
		configurationUnits( null );
	}

	@Test
	public void correctDomainTypeReturned() throws Exception {
		assertEquals( configurationUnits( DomainType.class ).getDomainType(), DomainType.class );
	}

	@Test
	public void existingUnitFound() throws Exception {
		final ConfigurationUnits testee = configurationUnits( DomainType.class, configurationUnitFor( CONFIGURATION, EntityMetadataConfigurationUnit.class ) );

		assertNotNull( testee.getEntityConfiguration() );
	}

	@Test
	public void nullReturnedForMissingConfigurationUnit() throws Exception {
		final ConfigurationUnits testee = configurationUnits( DomainType.class, configurationUnitFor( FILTERS ) );

		assertNull( testee.getScreenContext() );
	}

	@Test
	public void unitsOrderTheSameAsInitial() throws Exception {
		final ConfigurationUnits testee = configurationUnits( DomainType.class, configurationUnitsFor( FILTERS, CONFIGURATION, SCOPES ) );

		assertUnitsOrderEqual( testee, new DomainConfigurationUnitType[] {FILTERS, CONFIGURATION, SCOPES} );
	}

	private void assertUnitsOrderEqual( final ConfigurationUnits testee, final DomainConfigurationUnitType[] expectedOrderedUnitTypes ) {
		Set<DomainConfigurationUnitType> actualUnitTypes = newLinkedHashSet();
		for ( ConfigurationUnit configurationUnit : testee ) {
			actualUnitTypes.add( configurationUnit.getDomainConfigurationUnitType() );
		}

		assertArrayEquals( actualUnitTypes.toArray(), expectedOrderedUnitTypes );
	}

	private ConfigurationUnits configurationUnits( Class<?> domainType, ConfigurationUnit... configurationUnits ) {
		return new ConfigurationUnits( domainType, configurationUnits );
	}

	private ConfigurationUnit[] configurationUnitsFor( DomainConfigurationUnitType... unitTypes ) {
		final Set<ConfigurationUnit> configurationUnits = newLinkedHashSet();
		for ( DomainConfigurationUnitType unitType : unitTypes ) {
			configurationUnits.add( configurationUnitFor( unitType ) );
		}
		return configurationUnits.toArray( new ConfigurationUnit[configurationUnits.size()] );
	}

	private ConfigurationUnit configurationUnitFor( DomainConfigurationUnitType unitType, Class<? extends ConfigurationUnit> configurationUnitInterface ) {
		ConfigurationUnit configurationUnit = EasyMock.createMock( configurationUnitInterface );
		EasyMock.expect( configurationUnit.getDomainConfigurationUnitType() ).andReturn( unitType ).anyTimes();
		EasyMock.replay( configurationUnit );
		return configurationUnit;
	}

	private ConfigurationUnit configurationUnitFor( DomainConfigurationUnitType unitType ) {
		return configurationUnitFor( unitType, ConfigurationUnit.class );
	}

	private static class DomainType {

	}
}