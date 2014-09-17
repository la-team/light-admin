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
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.*;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.support.DomainObjectMerger;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import java.util.Collection;
import java.util.Iterator;

import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.getPersistentField;
import static org.springframework.data.rest.core.support.DomainObjectMerger.NullHandlingPolicy.APPLY_NULLS;
import static org.springframework.util.ObjectUtils.nullSafeEquals;

public class DynamicDomainObjectMerger extends DomainObjectMerger {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDomainObjectMerger.class);

    private final GlobalAdministrationConfiguration configuration;

    public DynamicDomainObjectMerger(Repositories repositories, ConversionService conversionService, GlobalAdministrationConfiguration configuration) {
        super(repositories, conversionService);

        this.configuration = configuration;
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

        final BeanWrapper fromWrapper = beanWrapper(from);
        final BeanWrapper targetWrapper = beanWrapper(target);

        final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forManagedDomainType(target.getClass());
        final PersistentEntity<?, ?> entity = domainTypeAdministrationConfiguration.getPersistentEntity();

        entity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> persistentProperty) {
                Object sourceValue = fromWrapper.getPropertyValue(persistentProperty.getName());
                Object targetValue = targetWrapper.getPropertyValue(persistentProperty.getName());

                if (entity.isIdProperty(persistentProperty)) {
                    return;
                }

                if (nullSafeEquals(sourceValue, targetValue)) {
                    return;
                }

                if (propertyIsHiddenInFormView(persistentProperty, domainTypeAdministrationConfiguration)) {
                    return;
                }

                if (nullPolicy == APPLY_NULLS || sourceValue != null) {
                    targetWrapper.setPropertyValue(persistentProperty.getName(), sourceValue);
                }
            }
        });

        entity.doWithAssociations(new SimpleAssociationHandler() {
            @Override
            @SuppressWarnings("unchecked")
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                PersistentProperty<?> persistentProperty = association.getInverse();

                Object fromValue = fromWrapper.getPropertyValue(persistentProperty.getName());
                Object targetValue = targetWrapper.getPropertyValue(persistentProperty.getName());

                if (propertyIsHiddenInFormView(persistentProperty, domainTypeAdministrationConfiguration)) {
                    return;
                }

                if ((fromValue == null && nullPolicy == APPLY_NULLS)) {
                    targetWrapper.setPropertyValue(persistentProperty.getName(), fromValue);
                }

                if (persistentProperty.isCollectionLike()) {
                    Collection<Object> sourceCollection = (Collection) fromValue;
                    Collection<Object> targetCollection = (Collection) targetValue;

                    Collection<Object> candidatesForAddition = candidatesForAddition(sourceCollection, targetCollection, persistentProperty);
                    Collection<Object> candidatesForRemoval = candidatesForRemoval(sourceCollection, targetCollection, persistentProperty);

                    removeReferencedItems(targetCollection, candidatesForRemoval);

                    addReferencedItems(targetCollection, candidatesForAddition);

                    return;
                }

                if (!nullSafeEquals(fromValue, targetWrapper.getPropertyValue(persistentProperty.getName()))) {
                    targetWrapper.setPropertyValue(persistentProperty.getName(), fromValue);
                }
            }
        });
    }

    private boolean propertyIsHiddenInFormView(PersistentProperty persistentProperty, DomainTypeAdministrationConfiguration configuration) {
        PersistentFieldMetadata persistentField = getPersistentField(configuration.getFormViewFragment().getFields(), persistentProperty.getName());

        return persistentField == null;
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
        final PersistentEntity<?, ?> persistentEntity = configuration.forManagedDomainType(item.getClass()).getPersistentEntity();
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

        Object sourceItemIdValue = beanWrapper(item1).getPropertyValue(idProperty.getName());
        Object itemIdValue = beanWrapper(item2).getPropertyValue(idProperty.getName());

        return nullSafeEquals(itemIdValue, sourceItemIdValue);
    }

    private DirectFieldAccessFallbackBeanWrapper beanWrapper(Object item1) {
        return new DirectFieldAccessFallbackBeanWrapper(item1);
    }
}