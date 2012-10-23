package org.lightadmin.core.test.util;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

public class DummyConfigurationsHelper {

	public static DomainConfiguration emptyDomainEntityConfiguration() {
		return new DomainConfiguration( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );
	}

	public static DomainConfiguration domainEntityConfigurationWithException() {
		return new DomainConfiguration( domainTypeEntityMetadataMock( DomainEntity.class ), ConfigurationWithException.class );
	}

	public static DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver() {
		final DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver = EasyMock.createNiceMock( DomainTypeEntityMetadataResolver.class );
		EasyMock.expect( entityMetadataResolver.resolveEntityMetadata( EasyMock.<Class>anyObject() ) ).andAnswer( new IAnswer<DomainTypeEntityMetadata>() {
			@Override
			public DomainTypeEntityMetadata answer() throws Throwable {
				final Object[] currentArguments = EasyMock.getCurrentArguments();
				return DummyConfigurationsHelper.domainTypeEntityMetadataMock( ( Class ) currentArguments[0] );
			}
		} ).anyTimes();

		EasyMock.replay( entityMetadataResolver );

		return entityMetadataResolver;
	}

	public static DomainTypeEntityMetadata domainTypeEntityMetadataMock( Class domainType ) {
		DomainTypeEntityMetadata domainTypeEntityMetadata = EasyMock.createNiceMock( DomainTypeEntityMetadata.class );
		EasyMock.expect( domainTypeEntityMetadata.getDomainType() ).andReturn( domainType ).anyTimes();
		EasyMock.replay( domainTypeEntityMetadata );
		return domainTypeEntityMetadata;
	}

	public static class DomainEntity {

	}

	@SuppressWarnings( "unused" )
	@Administration( DomainEntity.class )
	public static class ConfigurationWithException {

		public static EntityConfiguration configuration( EntityConfigurationBuilder configurationBuilder ) {
			throw new RuntimeException();
		}
	}

	@Administration( DomainEntity.class )
	public static class DomainEntityEmptyConfiguration {

	}

}