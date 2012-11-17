package org.lightadmin.core.test.util;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

@SuppressWarnings( {"unchecked", "unused"} )
public abstract class DomainTypeEntityMetadataUtils {

	public static DomainTypeEntityMetadataResolver entityMetadataResolver( Class<?> domainType ) {
		DomainTypeEntityMetadataResolver entityMetadataResolver = EasyMock.createMock( DomainTypeEntityMetadataResolver.class );
		EasyMock.expect( entityMetadataResolver.resolveEntityMetadata( domainType ) ).andReturn( domainTypeEntityMetadata( domainType ) ).anyTimes();
		EasyMock.replay( entityMetadataResolver );
		return entityMetadataResolver;
	}

	public static DomainTypeEntityMetadataResolver entityMetadataResolver() {
		final DomainTypeEntityMetadataResolver entityMetadataResolver = EasyMock.createNiceMock( DomainTypeEntityMetadataResolver.class );
		EasyMock.expect( entityMetadataResolver.resolveEntityMetadata( EasyMock.<Class>anyObject() ) ).andAnswer( new IAnswer<DomainTypeEntityMetadata>() {
			@Override
			public DomainTypeEntityMetadata answer() throws Throwable {
				final Object[] currentArguments = EasyMock.getCurrentArguments();
				return domainTypeEntityMetadata( ( Class ) currentArguments[0] );
			}
		} ).anyTimes();

		EasyMock.replay( entityMetadataResolver );

		return entityMetadataResolver;
	}

	public static DomainTypeEntityMetadataResolver entityMetadataResolver( DomainTypeEntityMetadata... domainTypeEntityMetadatas ) {
		final DomainTypeEntityMetadataResolver entityMetadataResolver = EasyMock.createNiceMock( DomainTypeEntityMetadataResolver.class );

		for ( DomainTypeEntityMetadata domainTypeEntityMetadata : domainTypeEntityMetadatas ) {
			EasyMock.expect( entityMetadataResolver.resolveEntityMetadata( domainTypeEntityMetadata.getDomainType() ) ).andReturn( domainTypeEntityMetadata ).anyTimes();
		}

		EasyMock.replay( entityMetadataResolver );

		return entityMetadataResolver;
	}

	public static DomainTypeEntityMetadata domainTypeEntityMetadata( Class<?> domainType ) {
		DomainTypeEntityMetadata domainTypeEntityMetadata = EasyMock.createMock( DomainTypeEntityMetadata.class );
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
}