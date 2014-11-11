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

import static java.lang.String.format;

@SuppressWarnings("unused")
public class InvalidPropertyConfigurationProblem extends DomainConfigurationProblem {

    private InvalidPropertyConfigurationProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String message, ProblemLevel problemLevel) {
        super(configurationUnits, configurationUnitType, message, problemLevel);
    }

    public static InvalidPropertyConfigurationProblem missingFieldProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Missing property '%s' defined!", propertyName), ProblemLevel.ERROR);
    }

    public static InvalidPropertyConfigurationProblem rendererNotDefinedForFieldProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Renderer not defined for custom property '%s'!", propertyName), ProblemLevel.ERROR);
    }

    public static InvalidPropertyConfigurationProblem notSupportedTypeFieldProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Persistent property '%s' has not supported type!", propertyName), ProblemLevel.ERROR);
    }

    public static InvalidPropertyConfigurationProblem missingBaseDirectoryInFileReferenceProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("@FileReference property '%s' has incorrect baseDirectory defined!", propertyName), ProblemLevel.WARNING);
    }

    public static InvalidPropertyConfigurationProblem invalidPropertyValueExpressionProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Property '%s' has invalid path/expression defined!", propertyName), ProblemLevel.ERROR);
    }
}