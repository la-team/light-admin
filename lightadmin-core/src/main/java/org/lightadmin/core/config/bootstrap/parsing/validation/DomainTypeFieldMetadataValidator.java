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
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@SuppressWarnings("unchecked")
class DomainTypeFieldMetadataValidator implements FieldMetadataValidator<FieldMetadata> {

    private final Map<Class<? extends FieldMetadata>, FieldMetadataValidator<? extends FieldMetadata>> fieldMetadataValidators = newHashMap();

    public DomainTypeFieldMetadataValidator() {
        fieldMetadataValidators.put(PersistentFieldMetadata.class, new PersistentFieldMetadataValidator());
        fieldMetadataValidators.put(TransientFieldMetadata.class, new TransientFieldMetadataValidator());
        fieldMetadataValidators.put(CustomFieldMetadata.class, new CustomFieldMetadataValidator());
    }

    @Override
    public Collection<? extends DomainConfigurationProblem> validateFieldMetadata(FieldMetadata fieldMetadata, Class<?> domainType, DomainConfigurationValidationContext validationContext) {
        final FieldMetadataValidator fieldMetadataValidator = fieldMetadataValidators.get(fieldMetadata.getClass());

        return fieldMetadataValidator.validateFieldMetadata(fieldMetadata, domainType, validationContext);
    }
}