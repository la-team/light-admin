/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.config.domain.unit;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.core.config.domain.common.GenericFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.common.PersistentFieldSetConfigurationUnitBuilderAdapter;
import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.DefaultScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.DefaultFiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.DefaultScopesConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.sidebar.DefaultSidebarsConfigurationUnitBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import static org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType.*;
import static org.lightadmin.core.util.DomainConfigurationUtils.initializeConfigurationUnitWithBuilder;
import static org.lightadmin.core.util.DomainConfigurationUtils.isAnnotationBasedConfigurationCandidate;

public final class ConfigurationUnitsConverter {

    private static final ConfigurationUnitsConverter INSTANCE = new ConfigurationUnitsConverter();

    public static ConfigurationUnits unitsFromAutowiredConfiguration(Class configurationClass, AutowireCapableBeanFactory beanFactory) {
        return isAnnotationBasedConfigurationCandidate(configurationClass)
                ? INSTANCE.convertAnnotationBasedConfiguration(configurationClass)
                : INSTANCE.convertSuperClassBasedConfiguration(configurationClass, autowiredConfigurationInstance(configurationClass, beanFactory));
    }

    public static ConfigurationUnits unitsFromConfiguration(Class configurationClass) {
        return isAnnotationBasedConfigurationCandidate(configurationClass)
                ? INSTANCE.convertAnnotationBasedConfiguration(configurationClass)
                : INSTANCE.convertSuperClassBasedConfiguration(configurationClass, configurationInstance(configurationClass));
    }

    private static AdministrationConfiguration configurationInstance(final Class configurationClass) {
        return (AdministrationConfiguration) BeanUtils.instantiateClass(configurationClass);
    }

    private static AdministrationConfiguration autowiredConfigurationInstance(final Class configurationClass, AutowireCapableBeanFactory beanFactory) {
        final AdministrationConfiguration configurationInstance = configurationInstance(configurationClass);
        beanFactory.autowireBean(configurationInstance);
        return configurationInstance;
    }

    private ConfigurationUnits convertAnnotationBasedConfiguration(final Class configurationClass) {
        return new ConfigurationUnits(configurationClass,
                initializeConfigurationUnitWithBuilder(configurationClass, FILTERS, FiltersConfigurationUnitBuilder.class, DefaultFiltersConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationClass, SCOPES, ScopesConfigurationUnitBuilder.class, DefaultScopesConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationClass, SIDEBARS, SidebarsConfigurationUnitBuilder.class, DefaultSidebarsConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationClass, QUICK_VIEW, FieldSetConfigurationUnitBuilder.class, GenericFieldSetConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationClass, LIST_VIEW, FieldSetConfigurationUnitBuilder.class, GenericFieldSetConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationClass, SHOW_VIEW, FieldSetConfigurationUnitBuilder.class, GenericFieldSetConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationClass, FORM_VIEW, PersistentFieldSetConfigurationUnitBuilder.class, PersistentFieldSetConfigurationUnitBuilderAdapter.class),
                initializeConfigurationUnitWithBuilder(configurationClass, SCREEN_CONTEXT, ScreenContextConfigurationUnitBuilder.class, DefaultScreenContextConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationClass, CONFIGURATION, EntityMetadataConfigurationUnitBuilder.class, DefaultEntityMetadataConfigurationUnitBuilder.class));
    }

    private ConfigurationUnits convertSuperClassBasedConfiguration(final Class configurationClass, final AdministrationConfiguration configurationInstance) {
        return new ConfigurationUnits(
                configurationClass,
                initializeConfigurationUnitWithBuilder(configurationInstance, FILTERS, FiltersConfigurationUnitBuilder.class, DefaultFiltersConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, SCOPES, ScopesConfigurationUnitBuilder.class, DefaultScopesConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, SIDEBARS, SidebarsConfigurationUnitBuilder.class, DefaultSidebarsConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, QUICK_VIEW, FieldSetConfigurationUnitBuilder.class, GenericFieldSetConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, LIST_VIEW, FieldSetConfigurationUnitBuilder.class, GenericFieldSetConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, SHOW_VIEW, FieldSetConfigurationUnitBuilder.class, GenericFieldSetConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, FORM_VIEW, PersistentFieldSetConfigurationUnitBuilder.class, PersistentFieldSetConfigurationUnitBuilderAdapter.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, SCREEN_CONTEXT, ScreenContextConfigurationUnitBuilder.class, DefaultScreenContextConfigurationUnitBuilder.class),
                initializeConfigurationUnitWithBuilder(configurationInstance, CONFIGURATION, EntityMetadataConfigurationUnitBuilder.class, DefaultEntityMetadataConfigurationUnitBuilder.class)
        );
    }
}