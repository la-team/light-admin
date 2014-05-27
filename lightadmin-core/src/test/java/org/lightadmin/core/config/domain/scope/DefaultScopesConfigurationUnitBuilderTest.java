package org.lightadmin.core.config.domain.scope;

import org.junit.Test;
import org.lightadmin.api.config.builder.ScopesConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.ScopesConfigurationUnit;
import org.lightadmin.api.config.utils.DomainTypePredicates;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;

import static org.junit.Assert.*;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.all;
import static org.lightadmin.api.config.utils.ScopeMetadataUtils.filter;

public class DefaultScopesConfigurationUnitBuilderTest {

    @Test
    public void defaultConfigurationUnitCreatedForDomainType() {
        ScopesConfigurationUnit configurationUnit = defaultScopesBuilder().build();

        assertEquals(DomainConfigurationUnitType.SCOPES, configurationUnit.getDomainConfigurationUnitType());
        assertEquals(DomainType.class, configurationUnit.getDomainType());

        assertScopesDefined(configurationUnit, "All");
    }

    @Test
    public void allScopesDefined() throws Exception {
        ScopesConfigurationUnit configurationUnit = defaultScopesBuilder()
                .scope("Scope #1", all())
                .scope("Scope #2", filter(DomainTypePredicates.alwaysTrue()))
                .build();

        assertScopesDefined(configurationUnit, "Scope #1", "Scope #2");
    }

    @Test
    public void defaultScopeSetByDefault() throws Exception {
        ScopesConfigurationUnit configurationUnit = defaultScopesBuilder()
                .scope("Scope #1", all())
                .scope("Scope #2", filter(DomainTypePredicates.alwaysTrue()))
                .build();

        assertTrue(configurationUnit.getScope("Scope #1").isDefaultScope());
    }

    @Test
    public void customSetDefaultScope() throws Exception {
        ScopesConfigurationUnit configurationUnit = defaultScopesBuilder()
                .scope("Scope #1", all())
                .scope("Scope #2", filter(DomainTypePredicates.alwaysTrue()).defaultScope(true))
                .build();

        assertTrue(configurationUnit.getScope("Scope #2").isDefaultScope());
    }

    private void assertScopesDefined(ScopesConfigurationUnit configurationUnit, String... scopeNames) {
        for (String scopeName : scopeNames) {
            assertNotNull(configurationUnit.getScope(scopeName));
        }
    }

    private ScopesConfigurationUnitBuilder defaultScopesBuilder() {
        return new DefaultScopesConfigurationUnitBuilder(DomainType.class);
    }

    private static class DomainType {

    }
}