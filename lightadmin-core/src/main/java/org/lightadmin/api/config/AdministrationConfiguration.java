package org.lightadmin.api.config;

import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.*;

@SuppressWarnings("unused")
public abstract class AdministrationConfiguration<T> {

    public EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder.build();
    }

    public ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.build();
    }

    public FieldSetConfigurationUnit listView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder.build();
    }

    public FieldSetConfigurationUnit quickView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder.build();
    }

    public FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder.build();
    }

    public FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder.build();
    }

    public FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder.build();
    }

    public ScopesConfigurationUnit scopes(final ScopesConfigurationUnitBuilder scopeBuilder) {
        return scopeBuilder.build();
    }

    public SidebarsConfigurationUnit sidebars(final SidebarsConfigurationUnitBuilder sidebarsBuilder) {
        return sidebarsBuilder.build();
    }
}