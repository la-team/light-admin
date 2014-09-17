/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.config.domain.configuration.support;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.util.Transformer;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import static java.lang.String.format;

public abstract class ExceptionAwareTransformer implements Transformer<Object, String> {

    private ExceptionAwareTransformer() {
    }

    public static Transformer<Object, String> exceptionAwareNameExtractor(final EntityNameExtractor<Object> entityNameExtractor, final DomainTypeBasicConfiguration domainTypeBasicConfiguration) {
        return new ExceptionAwareTransformer() {
            @Override
            public String apply(final Object instance) {
                try {
                    return entityNameExtractor.apply(instance);
                } catch (Exception ex) {
                    BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(instance);

                    String domainTypeName = domainTypeBasicConfiguration.getDomainTypeName();
                    Object id = beanWrapper.getPropertyValue(domainTypeBasicConfiguration.getPersistentEntity().getIdProperty().getName());

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