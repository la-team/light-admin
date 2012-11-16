package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.test.util.ConfigurationUnitsUtils;

import static org.junit.Assert.assertEquals;
import static org.lightadmin.core.test.util.ConfigurationUnitsUtils.configurationUnitFor;

public class DomainConfigurationUnitsSourceTest {

	private DomainConfigurationUnitsSource testee;

	@Test( expected = IllegalArgumentException.class )
	public void nullDomainTypeEntityMetadataNotAllowed() {
		testee = new DomainConfigurationUnitsSource( null, domainTypeConfigurationUnits() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void nullConfigurationClassNotAllowed() {
		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata(), null );
	}

	@Test
	public void correctDomainTypeReturned() {
		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata(), domainTypeConfigurationUnits() );

		assertEquals( DomainType.class, testee.getDomainType() );
	}

	@Test
	public void correctConfigurationNameReturnedForDomainType() {
		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata(), domainTypeConfigurationUnits() );

		assertEquals( "DomainTypeConfiguration", testee.getConfigurationName() );
	}

	@Test
	public void correctEntityMetadataReturned() {
		final DomainTypeEntityMetadata expectedMetadata = domainTypeEntityMetadata();

		testee = new DomainConfigurationUnitsSource( expectedMetadata, domainTypeConfigurationUnits() );

		assertEquals( expectedMetadata, testee.getDomainTypeEntityMetadata() );
	}

	@Test
	public void correctConfigurationUnitsReturned() {
		ConfigurationUnits domainTypeConfigurationUnits = ConfigurationUnitsUtils.configurationUnits( DomainType.class,
			configurationUnitFor( DomainConfigurationUnitType.CONFIGURATION, EntityMetadataConfigurationUnit.class ),
			configurationUnitFor( DomainConfigurationUnitType.FILTERS, FiltersConfigurationUnit.class )
		);

		testee = new DomainConfigurationUnitsSource( domainTypeEntityMetadata(), domainTypeConfigurationUnits );

		assertEquals( domainTypeConfigurationUnits.getEntityConfiguration(), testee.getEntityConfiguration() );
		assertEquals( domainTypeConfigurationUnits.getFilters(), testee.getFilters() );
	}

	private DomainTypeEntityMetadata domainTypeEntityMetadata() {
		DomainTypeEntityMetadata domainTypeEntityMetadata = EasyMock.createMock( DomainTypeEntityMetadata.class );
		EasyMock.expect( domainTypeEntityMetadata.getDomainType() ).andReturn( DomainType.class ).anyTimes();
		EasyMock.replay( domainTypeEntityMetadata );

		return domainTypeEntityMetadata;
	}

	private ConfigurationUnits domainTypeConfigurationUnits() {
		return new ConfigurationUnits( DomainType.class );
	}

	private static class DomainType {

	}
}