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

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.core.AbstractEntityLinks;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class DomainEntityLinks extends AbstractEntityLinks {

    private final GlobalAdministrationConfiguration globalAdministrationConfiguration;
    private final LightAdminConfiguration lightAdminConfiguration;

    private final PluginRegistry<BackendIdConverter, Class<?>> idConverters;

    public DomainEntityLinks(GlobalAdministrationConfiguration globalAdministrationConfiguration, PluginRegistry<BackendIdConverter, Class<?>> idConverters, LightAdminConfiguration lightAdminConfiguration) {
        this.globalAdministrationConfiguration = globalAdministrationConfiguration;
        this.lightAdminConfiguration = lightAdminConfiguration;
        this.idConverters = idConverters;
    }

    @Override
    public boolean supports(Class<?> type) {
        return globalAdministrationConfiguration.isManagedDomainType(type);
    }

    public Link linkFor(PersistentEntityResource persistentEntityResource) {
        PersistentEntity persistentEntity = persistentEntityResource.getPersistentEntity();
        Object instance = persistentEntityResource.getContent();

        return linkToSingleResource(persistentEntity.getType(), idAttributeValue(instance, persistentEntity));
    }

    @Override
    public LinkBuilder linkFor(Class<?> type) {
        DomainTypeAdministrationConfiguration configuration = globalAdministrationConfiguration.forManagedDomainType(type);
        Assert.notNull(configuration, "You can't get a domain link to non-managed persistent entity");

        return new DomainLinkBuilder(lightAdminConfiguration).slash(configuration.getPluralDomainTypeName());
    }

    @Override
    public LinkBuilder linkFor(Class<?> type, Object... parameters) {
        return linkFor(type);
    }

    @Override
    public Link linkToCollectionResource(Class<?> type) {
        return linkFor(type).withSelfRel();
    }

    @Override
    public Link linkToSingleResource(Class<?> type, Object id) {
        if (id == null) {
            return linkFor(type).slash("new").withSelfRel();
        }

        Assert.isInstanceOf(Serializable.class, id, "Id must be assignable to Serializable!");

        String mappedId = idConverters.getPluginFor(type, BackendIdConverter.DefaultIdConverter.INSTANCE).toRequestId((Serializable) id, type);

        return linkFor(type).slash(mappedId).withSelfRel();
    }

    private Serializable idAttributeValue(Object entity, PersistentEntity persistentEntity) {
        return (Serializable) new DirectFieldAccessFallbackBeanWrapper(entity).getPropertyValue(persistentEntity.getIdProperty().getName());
    }
}