package org.lightadmin.core.config.domain.unit;

import static java.lang.String.format;

public enum DomainConfigurationUnitType {

    SCREEN_CONTEXT("screenContext"),
    CONFIGURATION("configuration"),
    LIST_VIEW("listView"),
    SHOW_VIEW("showView"),
    FORM_VIEW("formView"),
    QUICK_VIEW("quickView"),
    SCOPES("scopes"),
    FILTERS("filters"),
    SIDEBARS("sidebars");

    private final String name;

    private DomainConfigurationUnitType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DomainConfigurationUnitType forName(String name) {
        for (DomainConfigurationUnitType domainConfigurationUnitType : values()) {
            if (domainConfigurationUnitType.getName().equals(name)) {
                return domainConfigurationUnitType;
            }
        }
        throw new IllegalArgumentException(format("Configuration Unit for name %s not defined!", name));
    }

    @Override
    public String toString() {
        return format("Domain Configuration Unit: %s", getName());
    }
}