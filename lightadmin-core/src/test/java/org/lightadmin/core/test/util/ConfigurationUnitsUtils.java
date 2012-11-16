package org.lightadmin.core.test.util;

import org.easymock.EasyMock;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public abstract class ConfigurationUnitsUtils {

	public static ConfigurationUnits configurationUnits( Class<?> domainType, ConfigurationUnit... configurationUnits ) {
		return new ConfigurationUnits( domainType, configurationUnits );
	}

	public static ConfigurationUnit[] configurationUnitsFor( DomainConfigurationUnitType... unitTypes ) {
		final Set<ConfigurationUnit> configurationUnits = newLinkedHashSet();
		for ( DomainConfigurationUnitType unitType : unitTypes ) {
			configurationUnits.add( configurationUnitFor( unitType ) );
		}
		return configurationUnits.toArray( new ConfigurationUnit[configurationUnits.size()] );
	}

	public static ConfigurationUnit configurationUnitFor( DomainConfigurationUnitType unitType, Class<? extends ConfigurationUnit> configurationUnitInterface ) {
		ConfigurationUnit configurationUnit = EasyMock.createMock( configurationUnitInterface );
		EasyMock.expect( configurationUnit.getDomainConfigurationUnitType() ).andReturn( unitType ).anyTimes();
		EasyMock.replay( configurationUnit );
		return configurationUnit;
	}

	public static ConfigurationUnit configurationUnitFor( DomainConfigurationUnitType unitType ) {
		return configurationUnitFor( unitType, ConfigurationUnit.class );
	}

}