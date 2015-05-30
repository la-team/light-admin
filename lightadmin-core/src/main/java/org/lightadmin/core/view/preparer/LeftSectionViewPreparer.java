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
package org.lightadmin.core.view.preparer;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.beans.MenuItem;
import org.apache.tiles.request.Request;
import org.lightadmin.api.config.unit.SidebarsConfigurationUnit;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.view.preparer.support.DomainConfigToMenuItemTransformer;
import org.lightadmin.core.view.preparer.support.MenuItemComparator;
import org.lightadmin.core.web.support.DomainEntityLinks;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;

public class LeftSectionViewPreparer extends ConfigurationAwareViewPreparer {

    @Autowired
    private DomainEntityLinks domainEntityLinks;

    @Override
    protected void execute(final Request request, final AttributeContext attributeContext, final GlobalAdministrationConfiguration configuration) {
        addAttribute(attributeContext, "menuItems", menuItems(configuration.getManagedDomainTypeConfigurations().values()));
    }

    @Override
    protected void execute(final Request request, final AttributeContext attributeContext, final DomainTypeAdministrationConfiguration configuration) {
        final SidebarsConfigurationUnit sidebarsConfigurationUnit = configuration.getSidebars();
        final String selectedMenuItemName = configuration.getEntityConfiguration().getPluralName();

        addAttribute(attributeContext, "selectedMenuItemName", selectedMenuItemName);

        addAttribute(attributeContext, "sidebars", sidebarsConfigurationUnit.getSidebars());
    }

    private Collection<MenuItem> menuItems(Collection<DomainTypeAdministrationConfiguration> configurations) {
        final List<MenuItem> menuItems = newArrayList(transform(configurations, new DomainConfigToMenuItemTransformer(domainEntityLinks)));

        sort(menuItems, MenuItemComparator.INSTANCE);

        return menuItems;
    }
}