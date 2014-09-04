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
package org.lightadmin.core.web.support;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.storage.FileResourceStorage;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.lightadmin.core.web.util.ResponseUtils.*;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@SuppressWarnings("unused")
public class FileResourceLoader {

    private final GlobalAdministrationConfiguration configuration;
    private final FileResourceStorage fileResourceStorage;

    public FileResourceLoader(GlobalAdministrationConfiguration configuration, FileResourceStorage fileResourceStorage) {
        this.configuration = configuration;
        this.fileResourceStorage = fileResourceStorage;
    }

    public void downloadFile(Object entity, PersistentProperty<?> persistentProperty, HttpServletResponse response) throws IOException {
        final long size = fileResourceStorage.copy(entity, persistentProperty, response.getOutputStream());
        final String eTag = eTag(entity.getClass(), persistentProperty.getName(), size);

        addImageResourceHeaders(response, octetStreamResponseHeader(APPLICATION_OCTET_STREAM, size, eTag));
    }

    public void downloadFile(Object entity, String field, HttpServletResponse response) throws IOException {
        Class<?> domainType = entity.getClass();

        downloadFile(entity, attributeMetadata(domainType, field), response);
    }

    private PersistentProperty attributeMetadata(Class<?> domainType, String field) {
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forManagedDomainType(domainType);

        PersistentEntity persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();

        return persistentEntity.getPersistentProperty(field);
    }
}