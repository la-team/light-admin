package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.sidebar.SidebarsConfigurationUnit;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
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
