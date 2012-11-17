package org.lightadmin.core.config.domain.configuration.support;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import static org.junit.Assert.assertEquals;
import static org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory.*;

@SuppressWarnings( "unchecked" )
public class EntityNameExtractorFactoryTest {

	@Test
	public void simpleObjectNameExtraction() throws Exception {
		final EntityNameExtractor entityNameExtractor = forSimpleObject();

		assertEquals( "DomainType", entityNameExtractor.apply( new DomainType() ) );
	}

	@Test
	public void namedEntityNameExtraction() throws Exception {
		final EntityNameExtractor entityNameExtractor = forNamedPersistentEntity( "name" );

		assertEquals( "Domain Type Object Name", entityNameExtractor.apply( new DomainType() ) );
	}

	@Test
	public void persistentEntityNameExtraction() throws Exception {
		final DomainTypeEntityMetadata entityMetadata = domainTypeEntityMetadata( "Entity", 1 );

		final EntityNameExtractor entityNameExtractor = forPersistentEntity( entityMetadata );

		assertEquals( "Entity #1", entityNameExtractor.apply( new DomainType() ) );
	}

	private DomainTypeEntityMetadata domainTypeEntityMetadata( String entityName, int entityId ) {
		DomainTypeEntityMetadata domainTypeEntityMetadata = EasyMock.createMock( DomainTypeEntityMetadata.class );
		EasyMock.expect( domainTypeEntityMetadata.getEntityName() ).andReturn( entityName ).anyTimes();

		final DomainTypeAttributeMetadata idAttribute = EasyMock.createMock( DomainTypeAttributeMetadata.class );
		EasyMock.expect( idAttribute.getValue( EasyMock.anyObject() ) ).andReturn( entityId ).anyTimes();
		EasyMock.replay( idAttribute );

		EasyMock.expect( domainTypeEntityMetadata.getIdAttribute() ).andReturn( idAttribute ).anyTimes();

		EasyMock.replay( domainTypeEntityMetadata );

		return domainTypeEntityMetadata;
	}

	private static class DomainType {

		private String name = "Domain Type Object Name";

		public String getName() {
			return name;
		}
	}
}