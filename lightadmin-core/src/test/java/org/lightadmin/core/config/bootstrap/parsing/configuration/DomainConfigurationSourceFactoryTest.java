package org.lightadmin.core.config.bootstrap.parsing.configuration;

public class DomainConfigurationSourceFactoryTest {

//    private PersistentEntityResolver entityMetadataResolver;
//
//    private DomainConfigurationSourceFactory testee;
//
//    @Before
//    public void setUp() throws Exception {
//        entityMetadataResolver = entityMetadataResolver(DomainType.class);
//    }
//
//    @Test
//    public void postProcessorCalledForEachConfigurationUnit() throws Exception {
//        final ConfigurationUnit filtersConfigurationUnit = configurationUnitFor(FILTERS);
//        final ConfigurationUnit scopesConfigurationUnit = configurationUnitFor(SCOPES);
//
//        final ConfigurationUnits configurationUnits = configurationUnits(DomainTypeConfiguration.class, filtersConfigurationUnit, scopesConfigurationUnit);
//
//        final ConfigurationUnitPostProcessor configurationUnitPostProcessor = EasyMock.createMock(ConfigurationUnitPostProcessor.class);
//
//        EasyMock.expect(configurationUnitPostProcessor.postProcess(filtersConfigurationUnit, configurationUnits)).andReturn(filtersConfigurationUnit).once();
//
//        EasyMock.expect(configurationUnitPostProcessor.postProcess(scopesConfigurationUnit, configurationUnits)).andReturn(scopesConfigurationUnit).once();
//
//        EasyMock.replay(configurationUnitPostProcessor);
//
//        testee = new DomainConfigurationSourceFactory(entityMetadataResolver, EasyMock.createNiceMock(AutowireCapableBeanFactory.class), configurationUnitPostProcessor);
//
//        testee.domainConfigurationUnitsSource(configurationUnits);
//
//        EasyMock.verify(configurationUnitPostProcessor);
//    }
//
//    @Test
//    public void configurationSourceForDomainTypeCreated() throws Exception {
//        testee = new DomainConfigurationSourceFactory(entityMetadataResolver, EasyMock.createNiceMock(AutowireCapableBeanFactory.class), EasyMock.createNiceMock(ConfigurationUnitPostProcessor.class));
//
//        final Class<DomainType> expectedDomainType = DomainType.class;
//
//        final DomainConfigurationSource domainConfigurationSource = testee.domainConfigurationUnitsSource(configurationUnits(DomainTypeConfiguration.class));
//
//        assertNotNull(domainConfigurationSource);
//        assertEquals(expectedDomainType, domainConfigurationSource.getDomainType());
//    }
//
//    private static class DomainType {
//
//    }
//
//    @Administration(DomainType.class)
//    private static class DomainTypeConfiguration {
//
//    }
}
