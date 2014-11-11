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
package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.reporting.Problem;

@SuppressWarnings("unused")
public class DomainConfigurationProblem extends Problem {

    private final ConfigurationUnits configurationUnits;

    private final DomainConfigurationUnitType configurationUnitType;

    public DomainConfigurationProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, final String message, ProblemLevel problemLevel) {
        super(message, problemLevel);
        this.configurationUnits = configurationUnits;
        this.configurationUnitType = configurationUnitType;
    }

    public DomainConfigurationProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, final String message) {
        this(configurationUnits, configurationUnitType, message, ProblemLevel.ERROR);
    }

    public DomainConfigurationProblem(final ConfigurationUnits configurationUnits, final String message, ProblemLevel problemLevel) {
        this(configurationUnits, null, message, problemLevel);
    }

    public DomainConfigurationProblem(final ConfigurationUnits configurationUnits, final String message) {
        this(configurationUnits, message, ProblemLevel.ERROR);
    }

    @Override
    public String getMessage() {
        final StringBuilder stringBuilder = new StringBuilder("Domain Configuration ");
        stringBuilder.append("\"").append(configurationUnits.getConfigurationClassName()).append("\"").append(": ");
        if (configurationUnitType != null) {
            stringBuilder.append("Unit ").append("\"").append(configurationUnitType.getName()).append("\"").append(": ");
        }
        stringBuilder.append(super.getMessage());

        return stringBuilder.toString();
    }

    public ConfigurationUnits getConfigurationUnits() {
        return configurationUnits;
    }

    public DomainConfigurationUnitType getConfigurationUnitType() {
        return configurationUnitType;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}