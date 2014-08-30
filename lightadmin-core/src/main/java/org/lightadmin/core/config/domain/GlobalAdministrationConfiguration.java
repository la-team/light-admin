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