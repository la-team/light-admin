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
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.view.preparer.support.DomainConfigToMenuItemTransformer;
import org.lightadmin.core.view.preparer.support.MenuItemComparator;
import org.lightadmin.core.web.support.DomainEntityLinks;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.sort;

public class DashboardViewPreparer extends ConfigurationAwareViewPreparer {

    @Autowired
    private DomainEntityLinks domainEntityLinks;

    @Override
    protected void execute(final Request request, final AttributeContext attributeContext, final GlobalAdministrationConfiguration configuration) {
        super.execute(request, attributeContext, configuration);

        addAttribute(attributeContext, "dashboardDomainTypes", dashboardDomainTypes(configuration.getManagedDomainTypeConfigurations().values()));
    }

    private Collection<Pair<MenuItem, Long>> dashboardDomainTypes(Collection<DomainTypeAdministrationConfiguration> configurations) {
        final List<Pair<MenuItem, Long>> domainTypeItems = newLinkedList();
        for (DomainTypeAdministrationConfiguration configuration : configurations) {
            domainTypeItems.add(Pair.create(menuItem(configuration), configuration.getRepository().count()));
        }

        sort(domainTypeItems, new Comparator<Pair<MenuItem, Long>>() {
            @Override
            public int compare(final Pair<MenuItem, Long> menuItemPair1, final Pair<MenuItem, Long> menuItemPair2) {
                return MenuItemComparator.INSTANCE.compare(menuItemPair1.first, menuItemPair2.first);
            }
        });

        return domainTypeItems;
    }

    private MenuItem menuItem(final DomainTypeAdministrationConfiguration configuration) {
        return new DomainConfigToMenuItemTransformer(domainEntityLinks).apply(configuration);
    }
}