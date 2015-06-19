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

import org.apache.commons.lang3.BooleanUtils;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.ClassUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newLinkedList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.*;
import static org.lightadmin.core.util.NumberUtils.isNumber;
import static org.lightadmin.core.util.NumberUtils.parseNumber;

@SuppressWarnings("unchecked")
public class SpecificationCreator {

    private final ConversionService conversionService;
    private final GlobalAdministrationConfiguration configuration;

    public SpecificationCreator(final ConversionService conversionService, final GlobalAdministrationConfiguration configuration) {
        this.conversionService = conversionService;
        this.configuration = configuration;
    }

    public Specification toSpecification(final PersistentEntity persistentEntity, final Map<String, String[]> parameters) {
        return new Specification<Object>() {
            @Override
            public Predicate toPredicate(final Root<Object> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
                return new PredicatesBuilder(root, builder, persistentEntity).build(parameters);
            }
        };
    }

    private class PredicatesBuilder {

        private final Root<Object> root;
        private final CriteriaBuilder builder;
        private final PersistentEntity persistentEntity;

        private PredicatesBuilder(final Root<Object> root, final CriteriaBuilder builder, final PersistentEntity persistentEntity) {
            this.root = root;
            this.builder = builder;
            this.persistentEntity = persistentEntity;
        }

        public javax.persistence.criteria.Predicate build(final Map<String, String[]> parameters) {
            final List<Predicate> attributesPredicates = newLinkedList();

            for (String parameterName : parameters.keySet()) {
                if (persistentEntity.getPersistentProperty(parameterName) != null) {
                    PersistentProperty persistentProperty = persistentEntity.getPersistentProperty(parameterName);

                    final String attributeName = persistentProperty.getName();

                    final String[] parameterValues = parameters.get(attributeName);

                    attributesPredicates.add(attributePredicate(persistentProperty, attributeName, parameterValues));
                }
            }

            return builder.and(attributesPredicates.toArray(new Predicate[attributesPredicates.size()]));
        }

        private Predicate attributePredicate(PersistentProperty persistentProperty, final String attributeName, final String... parameterValues) {
            final String parameterValue = trim(parameterValues[0]);

            if (isNumericType(persistentProperty)) {
                return numericAttributePredicate(persistentProperty, attributeName, parameterValue);
            }

            if (isBooleanType(persistentProperty)) {
                return booleanAttributePredicate(attributeName, parameterValues);
            }

            if (isAssociation(persistentProperty)) {
                return associationAttributesPredicate(persistentProperty, attributeName, parameterValues);
            }

            if (isOfDateType(persistentProperty)) {
                return dateAttributePredicate(attributeName, parameterValue);
            }

            if (isOfTimeType(persistentProperty)) {
                return dateAttributePredicate(attributeName, parameterValue);
            }

            if (isOfDateTimeType(persistentProperty)) {
                return dateAttributePredicate(attributeName, parameterValue);
            }

            return stringAttributePredicate(attributeName, parameterValue);
        }

        private Predicate dateAttributePredicate(final String attributeName, final String parameterValue) {
            try {
                final Date date = Date.valueOf(parameterValue);

                return builder.equal(root.<String>get(attributeName), date);
            } catch (IllegalArgumentException e) {
                return builder.and();
            }
        }

        private Predicate stringAttributePredicate(final String attributeName, final String parameterValue) {
            if (isNotBlank(parameterValue)) {
                return builder.like(builder.lower(root.<String>get(attributeName)), "%" + parameterValue.toLowerCase() + "%");
            }

            return builder.and();
        }

        private Predicate associationAttributesPredicate(final PersistentProperty attribute, final String attributeName, final String... parameterValues) {
            final List<Predicate> attributesPredicates = newLinkedList();

            for (String parameterValue : parameterValues) {
                if (isNotBlank(parameterValue)) {
                    attributesPredicates.add(associationAttributePredicate(attribute, attributeName, parameterValue));
                }
            }

            return builder.and(attributesPredicates.toArray(new Predicate[attributesPredicates.size()]));
        }

        private Predicate associationAttributePredicate(final PersistentProperty attribute, final String attributeName, final String parameterValue) {
            final Class<?> domainType = attribute.getActualType();

            final DomainTypeBasicConfiguration domainTypeBasicConfiguration = domainTypeConfigurationFor(domainType);

            final JpaRepository repository = domainTypeBasicConfiguration.getRepository();

            final Serializable id = stringToSerializable(parameterValue, (Class<? extends Serializable>) domainTypeBasicConfiguration.getPersistentEntity().getIdProperty().getType());

            final Object entity = repository.findOne(id);

            if (attribute.isCollectionLike()) {
                final Expression<Collection> objectPath = root.get(attributeName);
                return builder.isMember(entity, objectPath);
            } else {
                return builder.equal(root.get(attributeName), entity);
            }
        }

        private Predicate booleanAttributePredicate(final String attributeName, final String... parameterValues) {
            final List<Predicate> attributesPredicates = newLinkedList();

            for (String parameterValue : parameterValues) {
                if (isNotBlank(parameterValue)) {
                    final boolean boolValue = BooleanUtils.toBoolean(parameterValue);

                    attributesPredicates.add(builder.equal(root.<String>get(attributeName), boolValue));
                }
            }

            return builder.or(attributesPredicates.toArray(new Predicate[attributesPredicates.size()]));
        }

        private Predicate numericAttributePredicate(final PersistentProperty attribute, final String attributeName, final String parameterValue) {
            if (isNumber(parameterValue)) {
                final Number attributeValue = parseNumber(parameterValue, (Class<? extends Number>) attribute.getType());

                return builder.equal(root.<String>get(attributeName), attributeValue);
            }

            return builder.and();
        }

        private boolean isOfDateType(final PersistentProperty attribute) {
            return PersistentPropertyType.forPersistentProperty(attribute) == DATE;
        }

        private boolean isOfTimeType(final PersistentProperty attribute) {
            return PersistentPropertyType.forPersistentProperty(attribute) == TIME;
        }

        private boolean isOfDateTimeType(final PersistentProperty attribute) {
            return PersistentPropertyType.forPersistentProperty(attribute) == DATE_TIME;
        }

        private boolean isAssociation(final PersistentProperty attribute) {
            return PersistentPropertyType.forPersistentProperty(attribute) == ASSOC || PersistentPropertyType.forPersistentProperty(attribute) == ASSOC_MULTI;
        }

        private boolean isBooleanType(final PersistentProperty attribute) {
            return PersistentPropertyType.forPersistentProperty(attribute) == BOOL;
        }

        private boolean isNumericType(final PersistentProperty attribute) {
            return PersistentPropertyType.forPersistentProperty(attribute) == NUMBER_INTEGER || PersistentPropertyType.forPersistentProperty(attribute) == NUMBER_FLOAT;
        }

        private DomainTypeBasicConfiguration domainTypeConfigurationFor(final Class<?> domainType) {
            final DomainTypeBasicConfiguration domainTypeBasicConfiguration = configuration.forDomainType(domainType);
            if (domainTypeBasicConfiguration != null) {
                return domainTypeBasicConfiguration;
            }

            return configuration.forManagedDomainType(domainType);
        }

        private <V extends Serializable> V stringToSerializable(String s, Class<V> targetType) {
            if (ClassUtils.isAssignable(targetType, String.class)) {
                return (V) s;
            }
            return conversionService.convert(s, targetType);
        }
    }
}