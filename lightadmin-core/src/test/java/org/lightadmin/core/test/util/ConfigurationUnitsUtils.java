package org.lightadmin.core.test.util;

import org.easymock.EasyMock;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.test.model.Address;
import org.lightadmin.core.test.model.Customer;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;

public abstract class ConfigurationUnitsUtils {

    public static ConfigurationUnits configurationUnits(final Class configurationClass, ConfigurationUnit... configurationUnits) {
        return new ConfigurationUnits(configurationClass, configurationUnits);
    }

    public static ConfigurationUnit[] configurationUnitsFor(DomainConfigurationUnitType... unitTypes) {
        final Set<ConfigurationUnit> configurationUnits = newLinkedHashSet();
        for (DomainConfigurationUnitType unitType : unitTypes) {
            configurationUnits.add(configurationUnitFor(unitType));
        }
        return configurationUnits.toArray(new ConfigurationUnit[configurationUnits.size()]);
    }

    public static ConfigurationUnit configurationUnitFor(DomainConfigurationUnitType unitType, Class<? extends ConfigurationUnit> configurationUnitInterface) {
        ConfigurationUnit configurationUnit = EasyMock.createMock(configurationUnitInterface);
        EasyMock.expect(configurationUnit.getDomainConfigurationUnitType()).andReturn(unitType).anyTimes();
        EasyMock.replay(configurationUnit);
        return configurationUnit;
    }

    public static ConfigurationUnit configurationUnitFor(DomainConfigurationUnitType unitType) {
        return configurationUnitFor(unitType, ConfigurationUnit.class);
    }

    private GlobalAdministrationConfiguration globalAdministrationConfiguration() {
        final Collection<DomainTypeAdministrationConfiguration> configurations = newArrayList(domainTypeAdministrationConfiguration(Address.class, "AddressConfiguration"), domainTypeAdministrationConfiguration(Customer.class, "CustomerConfiguration"));

        GlobalAdministrationConfiguration globalAdministrationConfiguration = EasyMock.createMock(GlobalAdministrationConfiguration.class);
        EasyMock.expect(globalAdministrationConfiguration.getDomainTypeConfigurationsValues()).andReturn(configurations).once();
        EasyMock.replay(globalAdministrationConfiguration);

        return globalAdministrationConfiguration;
    }

    private DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration(Class domainType, String configurationName) {
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = EasyMock.createMock(DomainTypeAdministrationConfiguration.class);
        EasyMock.expect(domainTypeAdministrationConfiguration.getDomainType()).andReturn(domainType).anyTimes();
        EasyMock.expect(domainTypeAdministrationConfiguration.getConfigurationName()).andReturn(configurationName).anyTimes();
        EasyMock.replay(domainTypeAdministrationConfiguration);
        return domainTypeAdministrationConfiguration;
    }
}