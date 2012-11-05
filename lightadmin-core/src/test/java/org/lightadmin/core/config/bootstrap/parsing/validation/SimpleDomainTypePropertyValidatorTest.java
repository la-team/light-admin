package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.junit.Test;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import static org.junit.Assert.assertTrue;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.domainTypeEntityMetadataMock;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.entityMetadataResolver;

public class SimpleDomainTypePropertyValidatorTest {

	private DomainTypePropertyValidator subject;

	@Test
	public void persistentExistingSimpleProperty() throws Exception {
		subject = new SimpleDomainTypePropertyValidator( entityMetadataResolver() );

		final DomainTypeEntityMetadata entityMetadata = domainTypeEntityMetadataMock( Entity.class, "nestedEntity" );

		assertTrue( subject.isValidProperty( "nestedEntity", entityMetadata ) );
	}

	@Test
	public void transientExistingSimpleProperty() throws Exception {
		subject = new SimpleDomainTypePropertyValidator( entityMetadataResolver() );

		final DomainTypeEntityMetadata entityMetadata = domainTypeEntityMetadataMock( Entity.class );

		assertTrue( subject.isInvalidProperty( "nestedEntity", entityMetadata ) );
	}

	@Test
	public void invalidSimpleProperty() throws Exception {
		subject = new SimpleDomainTypePropertyValidator( entityMetadataResolver() );

		final DomainTypeEntityMetadata entityMetadata = domainTypeEntityMetadataMock( Entity.class );

		assertTrue( subject.isInvalidProperty( "invalidProperty", entityMetadata ) );
	}

	@Test
	public void persistentEntityNestedProperty() {
		subject = new SimpleDomainTypePropertyValidator( entityMetadataResolver( domainTypeEntityMetadataMock( Entity.class, "nestedEntity" ), domainTypeEntityMetadataMock( NestedEntity.class, "nestedEntityProperty" ) ) );

		final DomainTypeEntityMetadata entityMetadata = domainTypeEntityMetadataMock( Entity.class, "nestedEntity" );

		assertTrue( subject.isValidProperty( "nestedEntity.nestedEntityProperty", entityMetadata ) );
	}

	@Test
	public void transientObjectNestedProperty() {
		subject = new SimpleDomainTypePropertyValidator( entityMetadataResolver( domainTypeEntityMetadataMock( Entity.class, "nestedEntity" ) ) );

		final DomainTypeEntityMetadata entityMetadata = domainTypeEntityMetadataMock( Entity.class, "nestedEntity" );

		assertTrue( subject.isInvalidProperty( "nestedEntity.nestedEntityProperty", entityMetadata ) );
	}

	@Test
	public void persistentEntityInvalidNestedProperty() {
		subject = new SimpleDomainTypePropertyValidator( entityMetadataResolver( domainTypeEntityMetadataMock( Entity.class, "nestedEntity" ), domainTypeEntityMetadataMock( NestedEntity.class, "nestedEntityProperty" ) ) );

		final DomainTypeEntityMetadata entityMetadata = domainTypeEntityMetadataMock( Entity.class, "nestedEntity" );

		assertTrue( subject.isInvalidProperty( "nestedEntity.invalidNestedProperty", entityMetadata ) );
	}

	@SuppressWarnings( "unused" )
	private static class Entity {

		private NestedEntity nestedEntity;

		public NestedEntity getNestedEntity() {
			return nestedEntity;
		}
	}

	@SuppressWarnings( "unused" )
	private static class NestedEntity {

		private String nestedEntityProperty;

		public String getNestedEntityProperty() {
			return nestedEntityProperty;
		}
	}
}