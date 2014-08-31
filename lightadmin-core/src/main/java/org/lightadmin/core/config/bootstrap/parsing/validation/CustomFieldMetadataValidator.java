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

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;

class CustomFieldMetadataValidator implements FieldMetadataValidator<CustomFieldMetadata> {

    @Override
    public Collection<? extends DomainConfigurationProblem> validateFieldMetadata(CustomFieldMetadata fieldMetadata, Class<?> domainType, DomainConfigurationValidationContext validationContext) {
        if (fieldMetadata.getRenderer() == null) {
            return newArrayList(validationContext.rendererNotDefinedForFieldProblem(fieldMetadata.getName()));
        }
        return emptyList();
    }
}