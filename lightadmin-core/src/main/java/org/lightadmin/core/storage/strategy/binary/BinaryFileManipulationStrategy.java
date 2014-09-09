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
package org.lightadmin.core.storage.strategy.binary;

import org.apache.commons.lang3.ArrayUtils;
import org.lightadmin.core.storage.FileManipulationStrategy;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.util.FileCopyUtils.copy;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class BinaryFileManipulationStrategy implements FileManipulationStrategy {

    private static final long ZERO_LENGTH = 0l;

    @Override
    public void deleteFile(Object entity, PersistentProperty persistentProperty) {
        setEmptyPropertyValue(entity, persistentProperty);
    }

    @Override
    public boolean fileExists(Object entity, PersistentProperty persistentProperty) throws IOException {
        return hasBinaryData(entity, persistentProperty);
    }

    @Override
    public byte[] loadFile(Object entity, PersistentProperty persistentProperty) throws IOException {
        return (byte[]) getPropertyValue(entity, persistentProperty);
    }

    @Override
    public long copyFile(Object entity, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
        return performBinaryDataCopy(entity, persistentProperty, outputStream);
    }

    @Override
    public void saveFile(Object entity, PersistentProperty persistentProperty, Object value) throws IOException {
        setPropertyValue(entity, persistentProperty, value);
    }

    @Override
    public void cleanup(Object entity, PersistentProperty persistentProperty) throws IOException {
        // NOOP
    }

    private long performBinaryDataCopy(Object entity, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
        if (hasBinaryData(entity, persistentProperty)) {
            byte[] fileData = (byte[]) getPropertyValue(entity, persistentProperty);
            copy(fileData, outputStream);
            return fileData.length;
        }
        return ZERO_LENGTH;
    }

    protected void setEmptyPropertyValue(Object entity, PersistentProperty persistentProperty) {
        setPropertyValue(entity, persistentProperty, null);
    }

    protected void setPropertyValue(Object entity, PersistentProperty persistentProperty, Object value) {
        beanWrapper(entity).setPropertyValue(persistentProperty.getName(), value);
    }

    protected DirectFieldAccessFallbackBeanWrapper beanWrapper(Object instance) {
        return new DirectFieldAccessFallbackBeanWrapper(instance);
    }

    protected boolean hasBinaryData(Object entity, PersistentProperty persistentProperty) {
        return ArrayUtils.isNotEmpty((byte[]) getPropertyValue(entity, persistentProperty));
    }

    protected Object getPropertyValue(Object entity, PersistentProperty persistentProperty) {
        return beanWrapper(entity).getPropertyValue(persistentProperty.getName());
    }
}