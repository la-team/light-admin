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

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.rest.core.event.*;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public abstract class ManagedRepositoryEventListener implements ApplicationListener<RepositoryEvent> {

    protected final GlobalAdministrationConfiguration configuration;

    protected ManagedRepositoryEventListener(GlobalAdministrationConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public final void onApplicationEvent(RepositoryEvent event) {
        Class<?> srcType = event.getSource().getClass();

        if (!configuration.isManagedDomainType(srcType)) {
            return;
        }

        if (event instanceof BeforeSaveEvent) {
            onBeforeSave(event.getSource());
        } else if (event instanceof BeforeCreateEvent) {
            onBeforeCreate(event.getSource());
        } else if (event instanceof AfterCreateEvent) {
            onAfterCreate(event.getSource());
        } else if (event instanceof AfterSaveEvent) {
            onAfterSave(event.getSource());
        } else if (event instanceof BeforeDeleteEvent) {
            onBeforeDelete(event.getSource());
        } else if (event instanceof AfterDeleteEvent) {
            onAfterDelete(event.getSource());
        }
    }

    /**
     * Override this method if you are interested in {@literal beforeCreate} events.
     *
     * @param entity The entity being created.
     */
    protected void onBeforeCreate(Object entity) {
    }

    /**
     * Override this method if you are interested in {@literal afterCreate} events.
     *
     * @param entity The entity that was created.
     */
    protected void onAfterCreate(Object entity) {
    }

    /**
     * Override this method if you are interested in {@literal beforeSave} events.
     *
     * @param entity The entity being saved.
     */
    protected void onBeforeSave(Object entity) {
    }

    /**
     * Override this method if you are interested in {@literal afterSave} events.
     *
     * @param entity The entity that was just saved.
     */
    protected void onAfterSave(Object entity) {
    }

    /**
     * Override this method if you are interested in {@literal beforeDelete} events.
     *
     * @param entity The entity that is being deleted.
     */
    protected void onBeforeDelete(Object entity) {
    }

    /**
     * Override this method if you are interested in {@literal afterDelete} events.
     *
     * @param entity The entity that was just deleted.
     */
    protected void onAfterDelete(Object entity) {
    }

    protected DomainTypeAdministrationConfiguration configurationFor(Class<?> clazz) {
        return this.configuration.forManagedDomainType(clazz);
    }

    protected PersistentEntity persistentEntityFor(Class<?> clazz) {
        return configurationFor(clazz).getPersistentEntity();
    }

    protected DynamicJpaRepository repositoryFor(Class<?> clazz) {
        return configurationFor(clazz).getRepository();
    }
}