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

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.support.Projector;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.util.Assert;

import java.io.Serializable;

import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

/**
 * Fix of Spring Data REST related defect
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class DynamicPersistentEntityResourceAssembler extends PersistentEntityResourceAssembler {

    public DynamicPersistentEntityResourceAssembler(PersistentEntityResourceAssembler resourceAssembler) {
        super(repositories(resourceAssembler), entityLinks(resourceAssembler), projector(resourceAssembler), mappings(resourceAssembler));
    }

    /**
     * @see DATAREST-269 (https://jira.spring.io/browse/DATAREST-269)
     */
    @Override
    public Link getSelfLinkFor(Object instance) {
        Assert.notNull(instance, "Domain object must not be null!");

        Repositories repositories = repositories(this);

        Class instanceType = instance.getClass();
        PersistentEntity<?, ?> entity = repositories.getPersistentEntity(instanceType);

        if (entity == null) {
            throw new IllegalArgumentException(String.format("Cannot create self link for %s! No persistent entity found!", instanceType));
        }

        EntityInformation<Object, Serializable> entityInformation = repositories.getEntityInformationFor(instanceType);
        Serializable id = entityInformation.getId(instance);

        if (id == null) {
            return entityLinks(this).linkToCollectionResource(entity.getType()).withSelfRel();
        }

        return entityLinks(this).linkToSingleResource(entity.getType(), id).withSelfRel();
    }

    private static Repositories repositories(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (Repositories) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("repositories");
    }

    private static EntityLinks entityLinks(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (EntityLinks) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("entityLinks");
    }

    private static Projector projector(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (Projector) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("projector");
    }

    private static ResourceMappings mappings(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (ResourceMappings) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("mappings");
    }
}