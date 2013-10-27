package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.api.config.unit.*;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public interface DomainConfigurationSource {

    Class<?> getDomainType();

    String getConfigurationName();

    DomainTypeEntityMetadata getDomainTypeEntityMetadata();

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
