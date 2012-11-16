package org.lightadmin.core.test.util;

import org.easymock.EasyMock;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

public abstract class DomainTypeEntityMetadataUtils {

	public static DomainTypeEntityMetadataResolver entityMetadataResolver( Class<?> domainType ) {
		DomainTypeEntityMetadataResolver entityMetadataResolver = EasyMock.createMock( DomainTypeEntityMetadataResolver.class );
		EasyMock.expect( entityMetadataResolver.resolveEntityMetadata( domainType ) ).andReturn( domainTypeEntityMetadata( domainType ) ).anyTimes();
		EasyMock.replay( entityMetadataResolver );
		return entityMetadataResolver;
	}

	public static DomainTypeEntityMetadata domainTypeEntityMetadata( Class<?> domainType ) {
		DomainTypeEntityMetadata domainTypeEntityMetadata = EasyMock.createMock( DomainTypeEntityMetadata.class );
		EasyMock.expect( domainTypeEntityMetadata.getDomainType() ).andReturn( domainType ).anyTimes();
		EasyMock.replay( domainTypeEntityMetadata );

		return domainTypeEntityMetadata;
	}
}