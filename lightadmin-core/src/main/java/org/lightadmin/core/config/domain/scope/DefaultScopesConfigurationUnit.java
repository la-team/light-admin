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
package org.lightadmin.core.config.domain.scope;

import org.lightadmin.api.config.unit.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultScopesConfigurationUnit extends DomainTypeConfigurationUnit implements ScopesConfigurationUnit {

    private final List<ScopeMetadata> scopesMetadata;

    DefaultScopesConfigurationUnit(Class<?> domainType, final List<ScopeMetadata> scopesMetadata) {
        super(domainType);

        Assert.notNull(scopesMetadata);

        this.scopesMetadata = newLinkedList(scopesMetadata);
    }

    @Override
    public Iterator<ScopeMetadata> iterator() {
        return scopesMetadata.iterator();
    }

    @Override
    public ScopeMetadata getScope(String name) {
        for (ScopeMetadata scope : scopesMetadata) {
            if (scope.getName().equalsIgnoreCase(name)) {
                return scope;
            }
        }
        return null;
    }

    @Override
    public DomainConfigurationUnitType getDomainConfigurationUnitType() {
        return DomainConfigurationUnitType.SCOPES;
    }

}