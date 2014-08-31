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

import static java.lang.String.format;

public enum DomainConfigurationUnitType {

    SCREEN_CONTEXT("screenContext"),
    CONFIGURATION("configuration"),
    LIST_VIEW("listView"),
    SHOW_VIEW("showView"),
    FORM_VIEW("formView"),
    QUICK_VIEW("quickView"),
    SCOPES("scopes"),
    FILTERS("filters"),
    SIDEBARS("sidebars");

    private final String name;

    private DomainConfigurationUnitType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DomainConfigurationUnitType forName(String name) {
        for (DomainConfigurationUnitType domainConfigurationUnitType : values()) {
            if (domainConfigurationUnitType.getName().equals(name)) {
                return domainConfigurationUnitType;
            }
        }
        throw new IllegalArgumentException(format("Configuration Unit for name %s not defined!", name));
    }

    @Override
    public String toString() {
        return getName();
    }
}