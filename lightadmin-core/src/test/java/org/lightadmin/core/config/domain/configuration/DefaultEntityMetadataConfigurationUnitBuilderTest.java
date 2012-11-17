package org.lightadmin.core.config.domain.configuration;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings( "unchecked" )
public class DefaultEntityMetadataConfigurationUnitBuilderTest {

	private DefaultEntityMetadataConfigurationUnitBuilder testee;

	@Test
	public void defaultConfigurationUnitCreatedForDomainType() throws Exception {
		testee = new DefaultEntityMetadataConfigurationUnitBuilder( DomainType.class );

		final EntityMetadataConfigurationUnit configurationUnit = testee.build();

		assertEquals( DomainConfigurationUnitType.CONFIGURATION, configurationUnit.getDomainConfigurationUnitType() );
		assertEquals( DomainType.class, configurationUnit.getDomainType() );
	}

	@Test
	public void configurationWithNameFieldExtractorCreated() throws Exception {
		testee = new DefaultEntityMetadataConfigurationUnitBuilder( DomainType.class );
		testee.nameField( "name" );

		final EntityMetadataConfigurationUnit configurationUnit = testee.build();

		assertNotNull( configurationUnit.getNameExtractor() );
		assertEquals( "Domain Type Object Name", configurationUnit.getNameExtractor().apply( new DomainType() ) );
	}

	@Test
	public void configurationWithNameExtractorCreated() throws Exception {
		final EntityNameExtractor expectedEntityNameExtractor = EasyMock.createNiceMock( EntityNameExtractor.class );

		testee = new DefaultEntityMetadataConfigurationUnitBuilder( DomainType.class );
		testee.nameExtractor( expectedEntityNameExtractor );

		final EntityMetadataConfigurationUnit configurationUnit = testee.build();

		assertNotNull( configurationUnit.getNameExtractor() );
		assertEquals( expectedEntityNameExtractor, configurationUnit.getNameExtractor() );
	}

	private static class DomainType {

		private String name = "Domain Type Object Name";

		public String getName() {
			return name;
		}
	}
}