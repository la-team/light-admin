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
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;

import java.io.Serializable;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class DynamicRepositoryEntityLinks implements EntityLinks {

    private EntityLinks delegate;

    private DynamicRepositoryEntityLinks(EntityLinks delegate) {
        this.delegate = delegate;
    }

    public static DynamicRepositoryEntityLinks wrap(EntityLinks delegate) {
        return new DynamicRepositoryEntityLinks(delegate);
    }

    public Link linkForFilePropertyLink(Object instance, PersistentProperty persistentProperty) {
        PersistentEntity persistentEntity = persistentProperty.getOwner();
        Serializable id = idValue(instance, persistentEntity);

        return delegate.linkForSingleResource(persistentEntity.getType(), id).slash(persistentProperty.getName()).slash("file").withSelfRel();
    }

    @Override
    public boolean supports(Class<?> delimiter) {
        return delegate.supports(delimiter);
    }

    @Override
    public LinkBuilder linkFor(Class<?> type) {
        return delegate.linkFor(type);
    }

    @Override
    public LinkBuilder linkFor(Class<?> type, Object... parameters) {
        return delegate.linkFor(type, parameters);
    }

    @Override
    public Link linkToCollectionResource(Class<?> type) {
        return delegate.linkToCollectionResource(type);
    }

    @Override
    public Link linkToSingleResource(Class<?> type, Object id) {
        if (id == null) {
            return linkFor(type).slash("new").withSelfRel();
        }
        return delegate.linkToSingleResource(type, id);
    }

    @Override
    public Link linkToSingleResource(Identifiable<?> entity) {
        return delegate.linkToSingleResource(entity);
    }

    @Override
    public LinkBuilder linkForSingleResource(Class<?> type, Object id) {
        return delegate.linkForSingleResource(type, id);
    }

    @Override
    public LinkBuilder linkForSingleResource(Identifiable<?> entity) {
        return delegate.linkForSingleResource(entity);
    }

    private Serializable idValue(Object instance, PersistentEntity persistentEntity) {
        return (Serializable) new DirectFieldAccessFallbackBeanWrapper(instance).getPropertyValue(persistentEntity.getIdProperty().getName());
    }
}