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
package org.lightadmin.api.config.utils;

import org.lightadmin.core.config.domain.scope.ScopeMetadata;

public abstract class ScopeMetadataUtils {

    private ScopeMetadataUtils() {
    }

    @SuppressWarnings("unchecked")
    public static ScopeMetadata scope(ScopeMetadata scope) {
        if (PredicateScopeMetadata.class.isAssignableFrom(scope.getClass())) {
            return new PredicateScopeMetadata((PredicateScopeMetadata) scope);
        }

        if (SpecificationScopeMetadata.class.isAssignableFrom(scope.getClass())) {
            return new SpecificationScopeMetadata((SpecificationScopeMetadata) scope);
        }

        return new DefaultScopeMetadata(scope);
    }

    public static ScopeMetadata all() {
        return new DefaultScopeMetadata().name("All");
    }

    public static <T> ScopeMetadata filter(DomainTypePredicate<T> filter) {
        return new PredicateScopeMetadata<T>(filter).name(filter.getClass().getSimpleName());
    }

    public static <T> ScopeMetadata specification(DomainTypeSpecification<T> specification) {
        return new SpecificationScopeMetadata<T>(specification).name(specification.getClass().getSimpleName());
    }

    public static boolean isSpecificationScope(final ScopeMetadata scope) {
        return scope instanceof ScopeMetadataUtils.SpecificationScopeMetadata;
    }

    public static boolean isPredicateScope(final ScopeMetadata scope) {
        return scope instanceof ScopeMetadataUtils.PredicateScopeMetadata;
    }

    public static class SpecificationScopeMetadata<T> extends AbstractScope {

        private final DomainTypeSpecification<T> specification;

        private SpecificationScopeMetadata(final DomainTypeSpecification<T> specification) {
            this.specification = specification;
        }

        private SpecificationScopeMetadata(final SpecificationScopeMetadata<T> scope) {
            super(scope);
            this.specification = scope.specification();
        }

        public DomainTypeSpecification<T> specification() {
            return specification;
        }
    }

    public static class PredicateScopeMetadata<T> extends AbstractScope {

        private DomainTypePredicate<T> predicate = DomainTypePredicates.alwaysTrue();

        private PredicateScopeMetadata(final DomainTypePredicate<T> predicate) {
            this.predicate = predicate;
        }

        public PredicateScopeMetadata(PredicateScopeMetadata<T> scope) {
            super(scope);
            this.predicate = scope.predicate();
        }

        public DomainTypePredicate<T> predicate() {
            return predicate;
        }
    }

    public static class DefaultScopeMetadata extends AbstractScope {

        private DefaultScopeMetadata() {
        }

        public DefaultScopeMetadata(ScopeMetadata scope) {
            super(scope);
        }
    }

    public static abstract class AbstractScope implements ScopeMetadata {

        private String name = "Undefined";

        private boolean defaultScope = false;

        protected AbstractScope() {
        }

        public AbstractScope(ScopeMetadata scope) {
            this.name = scope.getName();
            this.defaultScope = scope.isDefaultScope();
        }

        @Override
        public ScopeMetadata name(final String name) {
            this.name = name;
            return this;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isDefaultScope() {
            return this.defaultScope;
        }

        @Override
        public ScopeMetadata defaultScope(final boolean defaultScope) {
            this.defaultScope = defaultScope;
            return this;
        }
    }
}