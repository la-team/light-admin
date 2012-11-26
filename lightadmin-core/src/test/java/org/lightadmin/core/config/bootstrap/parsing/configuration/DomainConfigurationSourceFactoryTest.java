package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.support.ConfigurationUnitPostProcessor;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.FILTERS;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.SCOPES;
import static org.lightadmin.core.test.util.ConfigurationUnitsUtils.configurationUnitFor;
import static org.lightadmin.core.test.util.ConfigurationUnitsUtils.configurationUnits;
import static org.lightadmin.core.test.util.DomainTypeEntityMetadataUtils.entityMetadataResolver;

public class DomainConfigurationSourceFactoryTest {

	private DomainTypeEntityMetadataResolver entityMetadataResolver;

	private DomainConfigurationSourceFactory testee;

	@Before
	public void setUp() throws Exception {
		entityMetadataResolver = entityMetadataResolver( DomainType.class );
	}

	@Test
	public void postProcessorCalledForEachConfigurationUnit() throws Exception {
		final ConfigurationUnit filtersConfigurationUnit = configurationUnitFor( FILTERS );
		final ConfigurationUnit scopesConfigurationUnit = configurationUnitFor( SCOPES );

		final ConfigurationUnits configurationUnits = configurationUnits( DomainType.class, filtersConfigurationUnit, scopesConfigurationUnit );

		final ConfigurationUnitPostProcessor configurationUnitPostProcessor = EasyMock.createMock( ConfigurationUnitPostProcessor.class );

		EasyMock.expect( configurationUnitPostProcessor.postProcess( filtersConfigurationUnit ) ).andReturn( filtersConfigurationUnit ).once();

		EasyMock.expect( configurationUnitPostProcessor.postProcess( scopesConfigurationUnit ) ).andReturn( scopesConfigurationUnit ).once();

		EasyMock.replay( configurationUnitPostProcessor );

		testee = new DomainConfigurationSourceFactory( entityMetadataResolver, configurationUnitPostProcessor );

		testee.domainConfigurationUnitsSource( configurationUnits );

		EasyMock.verify( configurationUnitPostProcessor );
	}

	@Test
	public void configurationSourceForDomainTypeCreated() throws Exception {
		testee = new DomainConfigurationSourceFactory( entityMetadataResolver, EasyMock.createNiceMock( ConfigurationUnitPostProcessor.class ) );

		final Class<DomainType> expectedDomainType = DomainType.class;

		final DomainConfigurationSource domainConfigurationSource = testee.domainConfigurationUnitsSource( configurationUnits( expectedDomainType ) );

		assertNotNull( domainConfigurationSource );
		assertEquals( expectedDomainType, domainConfigurationSource.getDomainType() );
	}

	private static class DomainType {

	}
}