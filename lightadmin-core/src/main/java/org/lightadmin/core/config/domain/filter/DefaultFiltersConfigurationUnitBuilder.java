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
package org.lightadmin.core.config.domain.filter;

import org.lightadmin.api.config.builder.FiltersConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.utils.FilterMetadataUtils;
import org.lightadmin.core.config.domain.renderer.Renderer;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class DefaultFiltersConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<FiltersConfigurationUnit> implements FiltersConfigurationUnitBuilder {

    private final Set<FilterMetadata> filtersMetadata = newLinkedHashSet();

    public DefaultFiltersConfigurationUnitBuilder(final Class<?> domainType) {
        super(domainType);
    }

    @Override
    public FiltersConfigurationUnitBuilder filter(final String filterName, String fieldName) {
        filtersMetadata.add(FilterMetadataUtils.filter(filterName, fieldName));
        return this;
    }

    @Override
    public FiltersConfigurationUnitBuilder filters(FilterMetadata... filters) {
        for (FilterMetadata filter : filters) {
            filtersMetadata.add(filter);
        }
        return this;
    }

    @Override
    public FiltersConfigurationUnitBuilder renderer(final Renderer renderer) {
        return this;
    }

    @Override
    public FiltersConfigurationUnit build() {
        return new DefaultFiltersConfigurationUnit(getDomainType(), filtersMetadata);
    }
}