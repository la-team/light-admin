package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.api.config.unit.*;
import org.springframework.data.mapping.PersistentEntity;

public interface DomainConfigurationSource {

    Class<?> getDomainType();

    String getConfigurationName();

    PersistentEntity getPersistentEntity();

    FiltersConfigurationUnit getFilters();

    ScopesConfigurationUnit getScopes();

    FieldSetConfigurationUnit getQuickViewFragment();

    FieldSetConfigurationUnit getListViewFragment();

    FieldSetConfigurationUnit getShowViewFragment();

    FieldSetConfigurationUnit getFormViewFragment();

    ScreenContextConfigurationUnit getScreenContext();

    EntityMetadataConfigurationUnit getEntityConfiguration();

    SidebarsConfigurationUnit getSidebars();
}
