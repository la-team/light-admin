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
package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.apache.commons.lang3.StringUtils;
import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;

class TransientFieldMetadataValidator implements FieldMetadataValidator<TransientFieldMetadata> {

    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("([a-zA-Z_$][a-zA-Z\\d_$]*)(\\.[a-zA-Z_$][a-zA-Z\\d_$]*)*");

    private static final String PROPERTY_SEPARATOR = ".";

    @Override
    public Collection<? extends DomainConfigurationProblem> validateFieldMetadata(TransientFieldMetadata fieldMetadata, Class<?> domainType, DomainConfigurationValidationContext validationContext) {
        final String propertyPath = fieldMetadata.getProperty();

        if (isBlank(propertyPath)) {
            return newArrayList(validationContext.invalidPropertyValueExpressionProblem(fieldMetadata.getName()));
        }

        final List<String> properties = properties(propertyPath);
        if (properties.isEmpty()) {
            return newArrayList(validationContext.invalidPropertyValueExpressionProblem(fieldMetadata.getName()));
        }

        final TypeInformation<?> typeInformation = ClassTypeInformation.from(domainType);

        final Iterator<String> propertiesIterator = properties.iterator();

        StringBuilder currentPropertyPath = null;
        while (propertiesIterator.hasNext()) {
            if (currentPropertyPath == null) {
                currentPropertyPath = new StringBuilder(propertiesIterator.next());
            } else {
                currentPropertyPath.append(PROPERTY_SEPARATOR).append(propertiesIterator.next());
            }

            if (typeInformation.getProperty(currentPropertyPath.toString()) == null) {
                return newArrayList(validationContext.invalidPropertyValueExpressionProblem(fieldMetadata.getName()));
            }
        }

        return emptyList();
    }

    private List<String> properties(final String propertyPath) {
        if (!PROPERTY_NAME_PATTERN.matcher(propertyPath).matches()) {
            return emptyList();
        }
        return newArrayList(StringUtils.split(propertyPath, PROPERTY_SEPARATOR));
    }
}