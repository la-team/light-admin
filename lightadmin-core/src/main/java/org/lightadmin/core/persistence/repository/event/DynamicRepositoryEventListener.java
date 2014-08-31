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
package org.lightadmin.core.persistence.repository.event;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.lightadmin.core.storage.OperationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

import java.io.IOException;

public class DynamicRepositoryEventListener extends AbstractRepositoryEventListener<Object> {

    private final GlobalAdministrationConfiguration configuration;
    private final OperationBuilder operationBuilder;

    @Autowired
    public DynamicRepositoryEventListener(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        this.configuration = configuration;
        this.operationBuilder = OperationBuilder.operationBuilder(configuration, lightAdminConfiguration);
    }

    @Override
    protected void onAfterSave(final Object entity) {
        Class<?> domainType = entity.getClass();
        if (!configuration.isManagedDomainType(domainType)) {
            return;
        }

        PersistentEntity<?, ?> persistentEntity = configuration.forManagedDomainType(domainType).getPersistentEntity();

        persistentEntity.doWithProperties(new PersistentPropertyCleanupHandler(entity));
    }

    @Override
    protected void onBeforeDelete(final Object entity) {
        Class<?> domainType = entity.getClass();
        if (!configuration.isManagedDomainType(domainType)) {
            return;
        }

        PersistentEntity<?, ?> persistentEntity = configuration.forManagedDomainType(domainType).getPersistentEntity();

        persistentEntity.doWithProperties(new PersistentPropertyFileDeletionHandler(entity));
    }

    private class PersistentPropertyCleanupHandler implements SimplePropertyHandler {
        private final Object entity;

        public PersistentPropertyCleanupHandler(Object entity) {
            this.entity = entity;
        }

        @Override
        public void doWithPersistentProperty(PersistentProperty<?> property) {
            if (PersistentPropertyType.isOfFileReferenceType(property)) {
                try {
                    operationBuilder.saveOperation(entity).performCleanup(property);
                } catch (IOException e) {
                }
            }
        }
    }

    private class PersistentPropertyFileDeletionHandler implements SimplePropertyHandler {
        private final Object entity;

        public PersistentPropertyFileDeletionHandler(Object entity) {
            this.entity = entity;
        }

        @Override
        public void doWithPersistentProperty(PersistentProperty<?> property) {
            if (PersistentPropertyType.isOfFileReferenceType(property)) {
                operationBuilder.deleteOperation(entity).perform(property);
            }
        }
    }
}