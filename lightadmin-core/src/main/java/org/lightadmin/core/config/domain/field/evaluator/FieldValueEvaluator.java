package org.lightadmin.core.config.domain.field.evaluator;

import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareFieldValueRenderer;

@SuppressWarnings("unchecked")
public class FieldValueEvaluator {

    public Object evaluate(FieldMetadata fieldMetadata, Object source) {
        if (fieldMetadata instanceof CustomFieldMetadata) {
            CustomFieldMetadata customFieldMetadata = (CustomFieldMetadata) fieldMetadata;

            return exceptionAwareFieldValueRenderer(customFieldMetadata.getRenderer()).apply(source);
        }

        if (fieldMetadata instanceof PersistentFieldMetadata) {
            PersistentFieldMetadata persistentFieldMetadata = (PersistentFieldMetadata) fieldMetadata;

            return getPropertyValue(persistentFieldMetadata.getField(), source);
        }

        TransientFieldMetadata transientFieldMetadata = (TransientFieldMetadata) fieldMetadata;

        return getPropertyValue(transientFieldMetadata.getProperty(), source);
    }

    private Object getPropertyValue(String property, Object source) {
        try {
            return new DirectFieldAccessFallbackBeanWrapper(source).getPropertyValue(property);
        } catch (Exception ex) {
            return null;
        }
    }
}