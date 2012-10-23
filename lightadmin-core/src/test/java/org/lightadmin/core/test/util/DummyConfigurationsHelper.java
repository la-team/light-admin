package org.lightadmin.core.test.util;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.beans.parsing.ConfigurationUnitPropertyFilter;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.filter.DefaultFilterBuilder;
import org.lightadmin.core.config.domain.filter.FilterBuilder;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.config.domain.fragment.TableFragment;
import org.lightadmin.core.config.domain.fragment.TableFragmentBuilder;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

@SuppressWarnings( {"unused", "unchecked"} )
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
				return domainTypeEntityMetadataMock( ( Class ) currentArguments[0] );
			}
		} ).anyTimes();

		EasyMock.replay( entityMetadataResolver );

		return entityMetadataResolver;
	}

	public static DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver( DomainTypeEntityMetadata... domainTypeEntityMetadatas ) {
		final DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver = EasyMock.createNiceMock( DomainTypeEntityMetadataResolver.class );

		for ( DomainTypeEntityMetadata domainTypeEntityMetadata : domainTypeEntityMetadatas ) {
			EasyMock.expect( entityMetadataResolver.resolveEntityMetadata( domainTypeEntityMetadata.getDomainType() ) ).andReturn( domainTypeEntityMetadata ).anyTimes();
		}

		EasyMock.replay( entityMetadataResolver );

		return entityMetadataResolver;
	}

	public static DomainTypeEntityMetadata domainTypeEntityMetadataMock( Class domainType ) {
		DomainTypeEntityMetadata domainTypeEntityMetadata = EasyMock.createNiceMock( DomainTypeEntityMetadata.class );
		EasyMock.expect( domainTypeEntityMetadata.getDomainType() ).andReturn( domainType ).anyTimes();
		EasyMock.replay( domainTypeEntityMetadata );
		return domainTypeEntityMetadata;
	}

	public static DomainTypeEntityMetadata domainTypeEntityMetadataMock( Class domainType, String... fieldNames ) {
		DomainTypeEntityMetadata domainTypeEntityMetadata = EasyMock.createNiceMock( DomainTypeEntityMetadata.class );
		EasyMock.expect( domainTypeEntityMetadata.getDomainType() ).andReturn( domainType ).anyTimes();

		for ( String fieldName : fieldNames ) {
			final DomainTypeAttributeMetadata attributeMetadata = EasyMock.createMock( DomainTypeAttributeMetadata.class );
			EasyMock.expect( attributeMetadata.getName() ).andReturn( fieldName ).anyTimes();
			EasyMock.replay( attributeMetadata );

			EasyMock.expect( domainTypeEntityMetadata.containsAttribute( fieldName ) ).andReturn( true ).anyTimes();

			EasyMock.expect( domainTypeEntityMetadata.getAttribute( fieldName ) ).andReturn( attributeMetadata ).anyTimes();
		}

		EasyMock.replay( domainTypeEntityMetadata );
		return domainTypeEntityMetadata;
	}

	public static Filters filters( String... fieldNames ) {
		FilterBuilder filterBuilder = new DefaultFilterBuilder();
		for ( String fieldName : fieldNames ) {
			filterBuilder.field( fieldName );
		}
		return filterBuilder.build();
	}

	public static TableFragment listView( String... fieldNames ) {
		FragmentBuilder fragmentBuilder = new TableFragmentBuilder();
		for ( String fieldName : fieldNames ) {
			fragmentBuilder.field( fieldName );
		}
		return ( TableFragment ) fragmentBuilder.build();
	}

	public static ConfigurationUnitPropertyFilter alwaysTrueFilter() {
		return new ConfigurationUnitPropertyFilter() {
			@Override
			public boolean apply( final String property ) {
				return true;
			}
		};
	}

	public static class DomainEntity {

	}

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