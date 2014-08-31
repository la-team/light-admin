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
package org.lightadmin.core.web.util;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.storage.OperationBuilder;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.lightadmin.core.util.ResponseUtils.*;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@SuppressWarnings("unused")
public class FileResourceLoader {

    private final GlobalAdministrationConfiguration globalAdministrationConfiguration;

    private final OperationBuilder operationBuilder;

    public FileResourceLoader(GlobalAdministrationConfiguration globalAdministrationConfiguration, LightAdminConfiguration lightAdminConfiguration) {
        this.globalAdministrationConfiguration = globalAdministrationConfiguration;

        this.operationBuilder = OperationBuilder.operationBuilder(globalAdministrationConfiguration, lightAdminConfiguration);
    }

    public void downloadFile(Object entity, PersistentProperty<?> persistentProperty, HttpServletResponse response) throws IOException {
        final long size = operationBuilder.getOperation(entity).performCopy(persistentProperty, response.getOutputStream());
        final String eTag = eTag(entity.getClass(), persistentProperty.getName(), size);

        addImageResourceHeaders(response, octetStreamResponseHeader(APPLICATION_OCTET_STREAM, size, eTag));
    }

    public void downloadFile(Object entity, String field, HttpServletResponse response) throws IOException {
        Class<?> domainType = entity.getClass();

        downloadFile(entity, attributeMetadata(domainType, field), response);
    }

    private PersistentProperty attributeMetadata(Class<?> domainType, String field) {
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = globalAdministrationConfiguration.forManagedDomainType(domainType);

        PersistentEntity persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();

        return persistentEntity.getPersistentProperty(field);
    }
}