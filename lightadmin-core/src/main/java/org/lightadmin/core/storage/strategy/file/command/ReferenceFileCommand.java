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
package org.lightadmin.core.storage.strategy.file.command;

import org.lightadmin.core.storage.strategy.file.FilePathResolver;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public abstract class ReferenceFileCommand {

    protected final FilePathResolver pathResolver;

    protected ReferenceFileCommand(FilePathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    protected void resetPropertyValue(Object entity, PersistentProperty attributeMetadata) {
        setPropertyValue(entity, attributeMetadata, null);
    }

    protected void setPropertyValue(Object entity, PersistentProperty persistentProperty, Object value) {
        beanWrapper(entity).setPropertyValue(persistentProperty.getName(), value);
    }

    protected Object getPropertyValue(Object entity, PersistentProperty persistentProperty) {
        return beanWrapper(entity).getPropertyValue(persistentProperty.getName());
    }

    private DirectFieldAccessFallbackBeanWrapper beanWrapper(Object instance) {
        return new DirectFieldAccessFallbackBeanWrapper(instance);
    }
}