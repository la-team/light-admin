package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;

import static java.lang.String.format;

@SuppressWarnings("unused")
public class InvalidPropertyConfigurationProblem extends DomainConfigurationProblem {

    private InvalidPropertyConfigurationProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String message) {
        super(configurationUnits, configurationUnitType, message);
    }

    public static InvalidPropertyConfigurationProblem missingFieldProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Missing property '%s' defined!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem rendererNotDefinedForFieldProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Renderer not defined for custom property '%s'!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem notSupportedTypeFieldProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Persistent property '%s' has not supported type!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem missingBaseDirectoryInFileReferenceProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("@FileReference property '%s' has incorrect baseDirectory defined!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem invalidPropertyValueExpressionProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(configurationUnits, configurationUnitType, format("Property '%s' has invalid path/expression defined!", propertyName));
    }
}