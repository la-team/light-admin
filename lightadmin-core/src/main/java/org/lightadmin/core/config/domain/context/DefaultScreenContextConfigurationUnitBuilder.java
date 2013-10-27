package org.lightadmin.core.config.domain.context;

import org.lightadmin.api.config.builder.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

public class DefaultScreenContextConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<ScreenContextConfigurationUnit> implements ScreenContextConfigurationUnitBuilder {

    private String screenName;

    public DefaultScreenContextConfigurationUnitBuilder(final Class<?> domainType) {
        super(domainType);

        this.screenName = domainType.getSimpleName();
    }

    @Override
    public ScreenContextConfigurationUnitBuilder screenName(final String screenName) {
        this.screenName = screenName;
        return this;
    }

    @Override
    public ScreenContextConfigurationUnit build() {
        return new DefaultScreenContextConfigurationUnit(getDomainType(), screenName);
    }
}