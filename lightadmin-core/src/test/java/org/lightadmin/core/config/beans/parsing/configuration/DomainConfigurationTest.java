package org.lightadmin.core.config.beans.parsing.configuration;

import org.junit.Test;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.*;

public class DomainConfigurationTest {

	private DomainConfiguration subject;

	@Test( expected = IllegalArgumentException.class )
	public void nullDomainTypeEntityMetadataNotAllowed() {
		subject = new DomainConfiguration( null, DomainEntityEmptyConfiguration.class );
	}

	@Test( expected = IllegalArgumentException.class )
	public void nullConfigurationClassNotAllowed() {
		subject = new DomainConfiguration( domainTypeEntityMetadataMock( DomainEntity.class ), null );
	}

	@Test
	public void correctDomainTypeReturned() {
		subject = emptyDomainEntityConfiguration();

		assertEquals( DomainEntity.class, subject.getDomainType() );
	}

	@Test
	public void correctEntityMetadataReturned() throws Exception {
		final DomainTypeEntityMetadata expectedMetadata = domainTypeEntityMetadataMock( DomainEntity.class );

		subject = new DomainConfiguration( expectedMetadata, DomainEntityEmptyConfiguration.class );

		assertEquals( expectedMetadata, subject.getDomainTypeEntityMetadata() );
	}

	@Test
	public void equalDomainConfigurations() throws Exception {
		final DomainConfiguration domainConfiguration = new DomainConfiguration( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );
		final DomainConfiguration otherDomainConfiguration = new DomainConfiguration( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );

		assertEquals( domainConfiguration, otherDomainConfiguration );
	}

	@Test
	public void notEqualDomainConfigurations() throws Exception {
		final DomainConfiguration domainConfiguration = new DomainConfiguration( domainTypeEntityMetadataMock( Object.class ), DomainEntityEmptyConfiguration.class );
		final DomainConfiguration otherDomainConfiguration = new DomainConfiguration( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );

		assertFalse( domainConfiguration.equals( otherDomainConfiguration ) );
	}
}