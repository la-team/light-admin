package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CustomFieldMetadataValidatorTest {

    private static final FieldValueRenderer<Object> NULL_RENDERER = null;

    private CustomFieldMetadataValidator testee;
    private DomainConfigurationValidationContext validationContext;

    @Before
    public void setUp() throws Exception {
        testee = new CustomFieldMetadataValidator();
        validationContext = new DomainConfigurationValidationContext(null, null, null, null, null);
    }

    @Test
    public void customFieldWithoutRendererFailure() {
        assertFalse(testee.validateFieldMetadata(customFieldMetadata(NULL_RENDERER), DomainType.class, validationContext).isEmpty());
    }

    @Test
    public void validFieldWithRendererDefined() throws Exception {
        assertTrue(testee.validateFieldMetadata(customFieldMetadata(EasyMock.createMock(FieldValueRenderer.class)), DomainType.class, validationContext).isEmpty());
    }

    private CustomFieldMetadata customFieldMetadata(final FieldValueRenderer<Object> renderer) {
        return new CustomFieldMetadata("Custom Field", renderer);
    }

    private static class DomainType {

    }
}