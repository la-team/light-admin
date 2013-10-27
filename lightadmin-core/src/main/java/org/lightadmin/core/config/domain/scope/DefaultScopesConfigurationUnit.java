package org.lightadmin.core.config.domain.scope;

import org.lightadmin.api.config.unit.ScopesConfigurationUnit;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
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