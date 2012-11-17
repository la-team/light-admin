package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomFieldMetadataValidatorTest {

	private static final FieldValueRenderer<Object> NULL_RENDERER = null;

	private CustomFieldMetadataValidator testee;

	@Before
	public void setUp() throws Exception {
		testee = new CustomFieldMetadataValidator();
	}

	@Test
	public void customFieldWithoutRendererFailure() {
		assertFalse( testee.isValidFieldMetadata( customFieldMetadata( NULL_RENDERER ), DomainType.class ) );
	}

	@Test
	public void validFieldWithRendererDefined() throws Exception {
		assertTrue( testee.isValidFieldMetadata( customFieldMetadata( EasyMock.createMock( FieldValueRenderer.class ) ), DomainType.class ) );
	}

	private CustomFieldMetadata customFieldMetadata( final FieldValueRenderer<Object> renderer ) {
		return new CustomFieldMetadata( "Custom Field", renderer );
	}

	private static class DomainType {

	}
}