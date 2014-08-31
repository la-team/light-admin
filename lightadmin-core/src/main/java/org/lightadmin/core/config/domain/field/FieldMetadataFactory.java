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
package org.lightadmin.core.config.domain.field;

import org.lightadmin.api.config.utils.FieldValueRenderer;

public abstract class FieldMetadataFactory {

    public static FieldMetadata persistentField(String name, String persistentField) {
        return new PersistentFieldMetadata(name, persistentField);
    }

    public static FieldMetadata transientField(final String name, final String transientProperty) {
        return new TransientFieldMetadata(name, transientProperty);
    }

    public static FieldMetadata customField(final String name, final FieldValueRenderer<Object> fieldValueRenderer) {
        return new CustomFieldMetadata(name, fieldValueRenderer);
    }
}