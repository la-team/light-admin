package org.lightadmin.core.config.domain.configuration.support;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.util.Transformer;
import org.springframework.data.mapping.model.BeanWrapper;

import static java.lang.String.format;

public abstract class ExceptionAwareTransformer implements Transformer<Object, String> {

    private ExceptionAwareTransformer() {
    }

    public static Transformer<Object, String> exceptionAwareNameExtractor(final EntityNameExtractor<Object> entityNameExtractor, final DomainTypeBasicConfiguration domainTypeBasicConfiguration) {
        return new ExceptionAwareTransformer() {
            @Override
            public String apply(final Object input) {
                try {
                    return entityNameExtractor.apply(input);
                } catch (Exception ex) {
                    String domainTypeName = domainTypeBasicConfiguration.getDomainTypeName();

                    BeanWrapper beanWrapper = BeanWrapper.create(input, null);

                    Object id = beanWrapper.getProperty(domainTypeBasicConfiguration.getPersistentEntity().getIdProperty());

                    return format("%s #%s", domainTypeName, String.valueOf(id));
                }
            }
        };
    }

    public static Transformer<Object, String> exceptionAwareFieldValueRenderer(final FieldValueRenderer<Object> fieldValueRenderer) {
        return new ExceptionAwareTransformer() {
            @Override
            public String apply(final Object input) {
                try {
                    return fieldValueRenderer.apply(input);
                } catch (Exception ex) {
                    return "";
                }
            }
        };
    }

    public abstract String apply(final Object input);
}