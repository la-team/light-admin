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
package org.lightadmin.core.storage;

import org.apache.commons.io.FileUtils;
import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.ClassUtils;

import java.io.File;

import static java.lang.String.valueOf;
import static org.apache.commons.io.FileUtils.getFile;

public abstract class AbstractFileRestOperation {

    protected final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration;

    protected final LightAdminConfiguration lightAdminConfiguration;

    protected final Object entity;

    protected AbstractFileRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        this.domainTypeAdministrationConfiguration = configuration.forManagedDomainType(ClassUtils.getUserClass(entity));
        this.lightAdminConfiguration = lightAdminConfiguration;
        this.entity = entity;
    }

    protected void resetAttrValue(PersistentProperty attributeMetadata) {
        performDirectSave(attributeMetadata, null);
    }

    protected void performDirectSave(PersistentProperty attrMeta, byte[] incomingVal) {
        BeanWrapper.create(entity, null).setProperty(attrMeta, incomingVal);
    }

    protected File referencedFileDomainEntityAttributeDirectory(PersistentProperty attrMeta) {
        final FileReference fileReference = (FileReference) attrMeta.findAnnotation(FileReference.class);
        return FileUtils.getFile(fileReference.baseDirectory(), FileStorageUtils.relativePathToDomainStorageAttributeDirectory(domainTypeName(), idAttributeValue(), attrMeta));
    }

    protected File fileStorageDomainEntityAttributeDirectory(PersistentProperty attrMeta) {
        return FileUtils.getFile(lightAdminConfiguration.getFileStorageDirectory(), FileStorageUtils.relativePathToDomainStorageAttributeDirectory(domainTypeName(), idAttributeValue(), attrMeta));
    }

    protected File referencedFileDomainEntityDirectory(PersistentProperty attrMeta) {
        final FileReference fileReference = (FileReference) attrMeta.findAnnotation(FileReference.class);
        return FileUtils.getFile(fileReference.baseDirectory(), FileStorageUtils.relativePathToDomainStorageDirectory(domainTypeName(), idAttributeValue()));
    }

    protected File fileStorageDomainEntityDirectory() {
        return FileUtils.getFile(lightAdminConfiguration.getFileStorageDirectory(), FileStorageUtils.relativePathToDomainStorageDirectory(domainTypeName(), idAttributeValue()));
    }

    protected File referencedFile(PersistentProperty persistentProperty) {
        final FileReference fileReference = (FileReference) persistentProperty.findAnnotation(FileReference.class);
        return getFile(fileReference.baseDirectory(), valueOf(BeanWrapper.create(entity, null).getProperty(persistentProperty)));
    }

    protected File fileStorageFile(PersistentProperty persistentProperty) {
        return getFile(lightAdminConfiguration.getFileStorageDirectory(), valueOf(BeanWrapper.create(entity, null).getProperty(persistentProperty)));
    }

    protected JpaRepository repository() {
        return domainTypeAdministrationConfiguration.getRepository();
    }

    protected Object idAttributeValue() {
        PersistentProperty idProperty = domainTypeAdministrationConfiguration.getPersistentEntity().getIdProperty();
        return new DirectFieldAccessFallbackBeanWrapper(entity).getPropertyValue(idProperty.getName());
    }

    protected String domainTypeName() {
        return domainTypeAdministrationConfiguration.getDomainTypeName();
    }
}