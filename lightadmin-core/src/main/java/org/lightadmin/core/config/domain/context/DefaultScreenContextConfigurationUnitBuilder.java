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