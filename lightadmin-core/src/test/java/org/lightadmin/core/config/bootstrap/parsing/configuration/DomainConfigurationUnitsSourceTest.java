package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.api.config.annotation.Administration;

public class DomainConfigurationUnitsSourceTest {

    private DomainConfigurationUnitsSource testee;

//    @Test(expected = IllegalArgumentException.class)
//    public void nullDomainTypeEntityMetadataNotAllowed() {
//        testee = new DomainConfigurationUnitsSource(null, domainTypeConfigurationUnits());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void nullConfigurationClassNotAllowed() {
//        testee = new DomainConfigurationUnitsSource(domainTypeEntityMetadata(DomainType.class), null);
//    }
//
//    @Test
//    public void correctDomainTypeReturned() {
//        testee = new DomainConfigurationUnitsSource(domainTypeEntityMetadata(DomainType.class), domainTypeConfigurationUnits());
//
//        assertEquals(DomainType.class, testee.getDomainType());
//    }
//
//    @Test
//    public void correctConfigurationNameReturnedForDomainType() {
//        testee = new DomainConfigurationUnitsSource(domainTypeEntityMetadata(DomainType.class), domainTypeConfigurationUnits());
//
//        assertEquals("DomainTypeConfiguration", testee.getConfigurationName());
//    }
//
//    @Test
//    public void correctEntityMetadataReturned() {
//        final DomainTypeEntityMetadata expectedMetadata = domainTypeEntityMetadata(DomainType.class);
//
//        testee = new DomainConfigurationUnitsSource(expectedMetadata, domainTypeConfigurationUnits());
//
//        assertEquals(expectedMetadata, testee.getDomainTypeEntityMetadata());
//    }
//
//    @Test
//    public void correctConfigurationUnitsReturned() {
//        final ConfigurationUnits domainTypeConfigurationUnits = ConfigurationUnitsUtils.configurationUnits(DomainTypeConfiguration.class, configurationUnitFor(DomainConfigurationUnitType.CONFIGURATION, EntityMetadataConfigurationUnit.class), configurationUnitFor(DomainConfigurationUnitType.FILTERS, FiltersConfigurationUnit.class));
//
//        testee = new DomainConfigurationUnitsSource(domainTypeEntityMetadata(DomainType.class), domainTypeConfigurationUnits);
//
//        assertEquals(domainTypeConfigurationUnits.getEntityConfiguration(), testee.getEntityConfiguration());
//        assertEquals(domainTypeConfigurationUnits.getFilters(), testee.getFilters());
//    }
//
//    private ConfigurationUnits domainTypeConfigurationUnits() {
//        return new ConfigurationUnits(DomainTypeConfiguration.class);
//    }

    private static class DomainType {

    }

    @Administration(DomainType.class)
    private static class DomainTypeConfiguration {

    }
}