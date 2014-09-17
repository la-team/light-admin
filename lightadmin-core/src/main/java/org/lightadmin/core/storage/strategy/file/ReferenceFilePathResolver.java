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
package org.lightadmin.core.storage.strategy.file;

import com.google.common.base.Joiner;
import org.lightadmin.api.config.annotation.FileReference;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import java.io.File;

import static java.io.File.separator;
import static java.lang.String.valueOf;
import static org.apache.commons.io.FileUtils.getFile;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class ReferenceFilePathResolver implements FilePathResolver {

    private static final String ROOT_DIRECTORY_NAME = "domain";
    private static final String FILE_NAME = "file.bin";

    private final PersistentEntity persistentEntity;
    private final String domainTypeName;
    private final File fileStorageDirectory;

    public ReferenceFilePathResolver(File fileStorageDirectory, PersistentEntity persistentEntity, String domainTypeName) {
        this.persistentEntity = persistentEntity;
        this.domainTypeName = domainTypeName;
        this.fileStorageDirectory = fileStorageDirectory;
    }

    @Override
    public File persistentPropertyFileReference(Object entity, PersistentProperty persistentProperty) {
        FileReference fileReference = fileReferenceAnnotation(persistentProperty);
        String relativeFilePath = persistentPropertyFileRelativePath(entity, persistentProperty);

        if (propertyBaseDirectoryExists(fileReference)) {
            return getFile(fileReference.baseDirectory(), relativeFilePath);
        }

        return getFile(this.fileStorageDirectory, relativeFilePath);
    }

    @Override
    public File persistentPropertyFileDirectory(Object entity, PersistentProperty persistentProperty) {
        FileReference fileReference = fileReferenceAnnotation(persistentProperty);

        if (propertyBaseDirectoryExists(fileReference)) {
            return getFile(fileReference.baseDirectory(), persistentPropertyFileDirectoryRelativePath(entity, persistentProperty));
        }
        return getFile(this.fileStorageDirectory, persistentPropertyFileDirectoryRelativePath(entity, persistentProperty));
    }

    @Override
    public String persistentPropertyFileRelativePath(Object entity, PersistentProperty persistentProperty) {
        if (propertyValueFileExists(entity, persistentProperty)) {
            return propertyValueAsString(entity, persistentProperty);
        }

        return Joiner.on(separator).join(persistentPropertyFileDirectoryRelativePath(entity, persistentProperty), FILE_NAME);
    }

    private boolean propertyValueFileExists(Object entity, PersistentProperty persistentProperty) {
        FileReference fileReference = fileReferenceAnnotation(persistentProperty);
        String propertyValue = propertyValueAsString(entity, persistentProperty);

        File fileDirectory = propertyBaseDirectoryExists(fileReference) ? getFile(fileReference.baseDirectory()) : this.fileStorageDirectory;

        File file = getFile(fileDirectory, propertyValue);

        return file.isFile() && file.exists();
    }

    private String persistentPropertyFileDirectoryRelativePath(Object entity, PersistentProperty persistentProperty) {
        return Joiner.on(separator).join(persistentEntityRootDirectoryRelativePath(entity), persistentProperty.getName());
    }

    private String persistentEntityRootDirectoryRelativePath(Object entity) {
        return Joiner.on(separator).join("", ROOT_DIRECTORY_NAME, this.domainTypeName, idPropertyValue(entity));
    }

    private FileReference fileReferenceAnnotation(PersistentProperty persistentProperty) {
        return (FileReference) persistentProperty.findAnnotation(FileReference.class);
    }

    private String idPropertyValue(Object entity) {
        return propertyValueAsString(entity, this.persistentEntity.getIdProperty());
    }

    private String propertyValueAsString(Object entity, PersistentProperty persistentProperty) {
        return valueOf(getPropertyValue(entity, persistentProperty));
    }

    private boolean propertyBaseDirectoryExists(FileReference fileReference) {
        return getFile(fileReference.baseDirectory()).exists();
    }

    private Object getPropertyValue(Object entity, PersistentProperty persistentProperty) {
        BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(entity);

        return beanWrapper.getPropertyValue(persistentProperty.getName());
    }
}