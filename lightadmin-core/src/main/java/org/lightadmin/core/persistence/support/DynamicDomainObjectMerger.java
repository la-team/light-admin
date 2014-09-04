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
package org.lightadmin.core.persistence.support;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.lightadmin.core.storage.FileResourceStorage;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.*;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.support.DomainObjectMerger;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.isOfFileReferenceType;
import static org.springframework.data.rest.core.support.DomainObjectMerger.NullHandlingPolicy.APPLY_NULLS;
import static org.springframework.util.ObjectUtils.nullSafeEquals;

public class DynamicDomainObjectMerger extends DomainObjectMerger {

    private final Repositories repositories;
    private final ConversionService conversionService;
    private final FileResourceStorage fileResourceStorage;

    public DynamicDomainObjectMerger(Repositories repositories, ConversionService conversionService, FileResourceStorage fileResourceStorage) {
        super(repositories, conversionService);

        this.repositories = repositories;
        this.conversionService = conversionService;
        this.fileResourceStorage = fileResourceStorage;
    }

    /**
     * Merges the given target object into the source one.
     *
     * @param from       can be {@literal null}.
     * @param target     can be {@literal null}.
     * @param nullPolicy how to handle {@literal null} values in the source object.
     */
    @Override
    public void merge(final Object from, final Object target, final NullHandlingPolicy nullPolicy) {
        if (from == null || target == null) {
            return;
        }

        final BeanWrapper<Object> fromWrapper = BeanWrapper.create(from, conversionService);
        final BeanWrapper<Object> targetWrapper = BeanWrapper.create(target, conversionService);
        final PersistentEntity<?, ?> entity = repositories.getPersistentEntity(target.getClass());

        entity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> persistentProperty) {
                Object sourceValue = fromWrapper.getProperty(persistentProperty);
                Object targetValue = targetWrapper.getProperty(persistentProperty);

                if (entity.isIdProperty(persistentProperty)) {
                    return;
                }

                if (nullSafeEquals(sourceValue, targetValue)) {
                    return;
                }

                if (isOfFileReferenceType(persistentProperty)) {
                    try {
                        fileResourceStorage.save(target, persistentProperty, sourceValue);
                        return;
                    } catch (IOException e) {
                    }
                }

                if (nullPolicy == APPLY_NULLS || sourceValue != null) {
                    targetWrapper.setProperty(persistentProperty, sourceValue);
                }
            }
        });

        entity.doWithAssociations(new SimpleAssociationHandler() {
            @Override
            @SuppressWarnings("unchecked")
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                PersistentProperty<?> persistentProperty = association.getInverse();

                Object fromValue = fromWrapper.getProperty(persistentProperty);
                Object targetValue = targetWrapper.getProperty(persistentProperty);

                if (persistentProperty.isCollectionLike()) {
                    Collection<Object> sourceCollection = (Collection) fromValue;
                    Collection<Object> targetCollection = (Collection) targetValue;

                    Collection<Object> candidatesForAddition = candidatesForAddition(sourceCollection, targetCollection, persistentProperty);
                    Collection<Object> candidatesForRemoval = candidatesForRemoval(sourceCollection, targetCollection, persistentProperty);

                    removeReferencedItems(targetCollection, candidatesForRemoval);

                    addReferencedItems(targetCollection, candidatesForAddition);

                    return;
                }

                if ((fromValue == null && nullPolicy == APPLY_NULLS) || !nullSafeEquals(fromValue, targetWrapper.getProperty(persistentProperty))) {
                    targetWrapper.setProperty(persistentProperty, fromValue);
                }
            }
        });
    }

    private void addReferencedItems(Collection<Object> targetCollection, Collection<Object> candidatesForAddition) {
        for (Object candidateForAddition : candidatesForAddition) {
            targetCollection.add(candidateForAddition);
        }
    }

    private void removeReferencedItems(Collection<Object> targetCollection, Collection<Object> candidatesForRemoval) {
        Iterator<Object> itr = targetCollection.iterator();
        while (itr.hasNext()) {
            Object obj = itr.next();
            if (mathesAny(candidatesForRemoval, obj)) {
                itr.remove();
            }
        }
    }

    private Collection<Object> candidatesForRemoval(Collection<Object> sourceCollection, Collection<Object> targetCollection, PersistentProperty<?> persistentProperty) {
        Collection<Object> candidatesForRemoval = newArrayList();
        for (Object targetItem : targetCollection) {
            if (!mathesAny(sourceCollection, targetItem)) {
                candidatesForRemoval.add(targetItem);
            }
        }
        return candidatesForRemoval;
    }

    private Collection<Object> candidatesForAddition(Collection<Object> sourceCollection, Collection<Object> targetCollection, PersistentProperty<?> persistentProperty) {
        Collection<Object> candidatesForAddition = newArrayList();
        for (Object sourceItem : sourceCollection) {
            if (!mathesAny(targetCollection, sourceItem)) {
                candidatesForAddition.add(sourceItem);
            }
        }
        return candidatesForAddition;
    }

    private boolean mathesAny(Collection<Object> collection, final Object item) {
        final PersistentEntity<?, ?> persistentEntity = repositories.getPersistentEntity(item.getClass());
        final PersistentProperty<?> idProperty = persistentEntity.getIdProperty();

        return Iterables.any(collection, new Predicate<Object>() {
            @Override
            public boolean apply(Object object) {
                return itemsEqual(object, item, idProperty);
            }
        });
    }

    private boolean itemsEqual(Object item1, Object item2, final PersistentProperty<?> idProperty) {
        if (nullSafeEquals(item1, item2)) {
            return true;
        }

        String sourceItemIdValue = BeanWrapper.create(item1, null).getProperty(idProperty).toString();
        String itemIdValue = BeanWrapper.create(item2, null).getProperty(idProperty).toString();

        return nullSafeEquals(itemIdValue, sourceItemIdValue);
    }
}