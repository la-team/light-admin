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
package org.lightadmin.core.config.domain.sidebar;

import org.lightadmin.api.config.builder.SidebarsConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.SidebarsConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultSidebarsConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<SidebarsConfigurationUnit> implements SidebarsConfigurationUnitBuilder {

    private final List<SidebarMetadata> sidebars = newLinkedList();

    public DefaultSidebarsConfigurationUnitBuilder(Class<?> domainType) {
        super(domainType);
    }

    @Override
    public SidebarsConfigurationUnitBuilder sidebar(String sidebarJspPath) {
        sidebars.add(new SidebarMetadata(sidebarJspPath));

        return this;
    }

    @Override
    public SidebarsConfigurationUnitBuilder sidebar(String name, String sidebarJspPath) {
        sidebars.add(new SidebarMetadata(name, sidebarJspPath));

        return this;
    }

    @Override
    public SidebarsConfigurationUnit build() {
        return new DefaultSidebarsConfigurationUnit(getDomainType(), sidebars);
    }
}