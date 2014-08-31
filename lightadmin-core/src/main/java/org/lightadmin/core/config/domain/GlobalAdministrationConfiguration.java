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
package org.lightadmin.core.config.domain;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newConcurrentMap;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@SuppressWarnings("unchecked")
public class GlobalAdministrationConfiguration {

    private final Map<Class<?>, DomainTypeAdministrationConfiguration> managedDomainTypeConfigurations = newConcurrentMap();
    private final Map<Class<?>, DomainTypeBasicConfiguration> domainTypeConfigurations = newConcurrentMap();

    public void registerDomainTypeConfiguration(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration) {
        managedDomainTypeConfigurations.put(domainTypeAdministrationConfiguration.getDomainType(), domainTypeAdministrationConfiguration);
        domainTypeConfigurations.put(domainTypeAdministrationConfiguration.getDomainType(), domainTypeAdministrationConfiguration);
    }

    public void registerNonDomainTypeConfiguration(DomainTypeBasicConfiguration domainTypeBasicConfiguration) {
        domainTypeConfigurations.put(domainTypeBasicConfiguration.getDomainType(), domainTypeBasicConfiguration);
    }

    public void removeDomainTypeConfiguration(final Class<?> domainType) {
        managedDomainTypeConfigurations.remove(domainType);
    }

    public void removeAllDomainTypeAdministrationConfigurations() {
        managedDomainTypeConfigurations.clear();

        domainTypeConfigurations.clear();
    }

    public Set<Class<?>> getManagedDomainTypes() {
        return managedDomainTypeConfigurations.keySet();
    }

    public Set<Class<?>> getNonManagedDomainTypes() {
        return domainTypeConfigurations.keySet();
    }

    public Set<Class<?>> getAllDomainTypes() {
        final Set<Class<?>> domainTypes = newLinkedHashSet();
        domainTypes.addAll(getNonManagedDomainTypes());
        domainTypes.addAll(getManagedDomainTypes());
        return domainTypes;
    }

    public Class<?>[] getAllDomainTypesAsArray() {
        Set<Class<?>> allDomainTypes = getAllDomainTypes();
        return allDomainTypes.toArray(new Class<?>[allDomainTypes.size()]);
    }

    public Map<Class<?>, DomainTypeAdministrationConfiguration> getManagedDomainTypeConfigurations() {
        return managedDomainTypeConfigurations;
    }

    public Map<Class<?>, DomainTypeBasicConfiguration> getDomainTypeConfigurations() {
        return domainTypeConfigurations;
    }

    public Collection<DomainTypeAdministrationConfiguration> getDomainTypeConfigurationsValues() {
        return managedDomainTypeConfigurations.values();
    }

    public DomainTypeAdministrationConfiguration forManagedDomainType(Class<?> domainType) {
        return managedDomainTypeConfigurations.get(domainType);
    }

    public boolean isManagedDomainType(Class<?> domainType) {
        return managedDomainTypeConfigurations.get(domainType) != null;
    }

    public DomainTypeBasicConfiguration forDomainType(Class<?> domainType) {
        return domainTypeConfigurations.get(domainType);
    }

    public DomainTypeAdministrationConfiguration forEntityName(String entityName) {
        for (DomainTypeAdministrationConfiguration configuration : managedDomainTypeConfigurations.values()) {
            if (equalsIgnoreCase(entityName, configuration.getDomainTypeName()) || equalsIgnoreCase(entityName, configuration.getPluralDomainTypeName())) {
                return configuration;
            }
        }
        throw new RuntimeException("Undefined entity name. Please check your configuration.");
    }
}