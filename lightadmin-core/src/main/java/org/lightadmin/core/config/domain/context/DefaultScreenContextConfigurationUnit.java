package org.lightadmin.core.config.domain.context;

import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;

public class DefaultScreenContextConfigurationUnit extends DomainTypeConfigurationUnit implements ScreenContextConfigurationUnit {

    private final String screenName;

    DefaultScreenContextConfigurationUnit(Class<?> domainType, final String screenName) {
        super(domainType);

        this.screenName = screenName;
    }

    @Override
    public String getScreenName() {
        return screenName;
    }

    @Override
    public DomainConfigurationUnitType getDomainConfigurationUnitType() {
        return DomainConfigurationUnitType.SCREEN_CONTEXT;
    }
}