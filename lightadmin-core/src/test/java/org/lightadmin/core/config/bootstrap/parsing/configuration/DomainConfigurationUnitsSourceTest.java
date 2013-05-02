package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.junit.Test;
import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.test.util.ConfigurationUnitsUtils;

import static org.junit.Assert.assertEquals;
import static org.lightadmin.core.test.util.ConfigurationUnitsUtils.configurationUnitFor;
import static org.lightadmin.core.test.util.DomainTypeEntityMetadataUtils.domainTypeEntityMetadata;

public class DomainConfigurationUnitsSourceTest {

	private DomainConfigurationUnitsSource testee;

	@Test( expected = IllegalArgumentException.class )
	public void nullDomainTypeEntityMetadataNotAllowed() {
		testee = new DomainConfigurationUnitsSource( null, domainTypeConfigurationUnits() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void nullConfigurationClassNotAllowed() {
		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata( DomainType.class ), null );
	}

	@Test
	public void correctDomainTypeReturned() {
		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata( DomainType.class ), domainTypeConfigurationUnits() );

		assertEquals( DomainType.class, testee.getDomainType() );
	}

	@Test
	public void correctConfigurationNameReturnedForDomainType() {
		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata( DomainType.class ), domainTypeConfigurationUnits() );

		assertEquals( "DomainTypeConfiguration", testee.getConfigurationName() );
	}

	@Test
	public void correctEntityMetadataReturned() {
		final DomainTypeEntityMetadata expectedMetadata = domainTypeEntityMetadata( DomainType.class );

		testee = new DomainConfigurationUnitsSource( expectedMetadata, domainTypeConfigurationUnits() );

		assertEquals( expectedMetadata, testee.getDomainTypeEntityMetadata() );
	}

	@Test
	public void correctConfigurationUnitsReturned() {
		final ConfigurationUnits domainTypeConfigurationUnits = ConfigurationUnitsUtils.configurationUnits( DomainTypeConfiguration.class, configurationUnitFor( DomainConfigurationUnitType.CONFIGURATION, EntityMetadataConfigurationUnit.class ), configurationUnitFor( DomainConfigurationUnitType.FILTERS, FiltersConfigurationUnit.class ) );

		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata( DomainType.class ), domainTypeConfigurationUnits );

		assertEquals( domainTypeConfigurationUnits.getEntityConfiguration(), testee.getEntityConfiguration() );
		assertEquals( domainTypeConfigurationUnits.getFilters(), testee.getFilters() );
	}

	private ConfigurationUnits domainTypeConfigurationUnits() {
		return new ConfigurationUnits( DomainTypeConfiguration.class );
	}

	private static class DomainType {

	}

	@Administration( DomainType.class )
	private static class DomainTypeConfiguration {

	}
}