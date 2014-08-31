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

import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

public class TransientFieldMetadata extends AbstractFieldMetadata {

    private final String property;

    public TransientFieldMetadata(final String name, final String property) {
        super(name);
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public Object getValue(Object entity) {
        return new DirectFieldAccessFallbackBeanWrapper(entity).getPropertyValue(property);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}