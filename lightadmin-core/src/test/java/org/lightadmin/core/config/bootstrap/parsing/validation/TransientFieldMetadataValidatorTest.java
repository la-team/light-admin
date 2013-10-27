package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransientFieldMetadataValidatorTest {

    private TransientFieldMetadataValidator testee;
    private DomainConfigurationValidationContext validationContext;

    @Before
    public void setUp() throws Exception {
        testee = new TransientFieldMetadataValidator();
        validationContext = new DomainConfigurationValidationContext(null, null, null, null, null);
    }

    @Test
    public void invalidEmptyProperty() throws Exception {
        assertInvalidProperty("");
    }

    @Test
    public void invalidMissingProperty() throws Exception {
        assertInvalidProperty("missingProperty");
    }

    @Test
    public void validPropertyWithGetter() throws Exception {
        assertValidProperty("propertyWithGetter");
    }

    @Test
    public void validPropertyWithoutGetter() throws Exception {
        assertValidProperty("property");
    }

    @Test
    public void validNestedProperty() throws Exception {
        assertValidProperty("childProperty.property");
    }

    @Test
    public void validBigNestedProperty() throws Exception {
        assertValidProperty("childProperty.childProperty.property");
    }

    @Test
    public void missingNestedProperty() throws Exception {
        assertInvalidProperty("childProperty.missingProperty");
    }

    @Test
    public void invalidPropertyPathFormatting() throws Exception {
        assertInvalidProperty("childProperty..property");
    }

    private void assertValidProperty(String property) {
        assertTrue(testee.validateFieldMetadata(transientFieldMetadata(property), ParentClass.class, validationContext).isEmpty());
    }

    private void assertInvalidProperty(String property) {
        assertFalse(testee.validateFieldMetadata(transientFieldMetadata(property), ParentClass.class, validationContext).isEmpty());
    }

    private TransientFieldMetadata transientFieldMetadata(final String property) {
        return new TransientFieldMetadata("Transient Field", property);
    }

    private static class ParentClass {

        private int property;
        private int propertyWithGetter;
        private ChildClass childProperty;

        public int getPropertyWithGetter() {
            return propertyWithGetter;
        }
    }

    private static class ChildClass {

        private int property;

        private ChildClass childProperty;
    }
}