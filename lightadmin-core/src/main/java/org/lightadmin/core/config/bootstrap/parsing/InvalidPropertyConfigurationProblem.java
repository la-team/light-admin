package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

import static java.lang.String.format;

@SuppressWarnings("unused")
public class InvalidPropertyConfigurationProblem extends DomainConfigurationProblem {

    private InvalidPropertyConfigurationProblem(final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType, String message) {
        super(domainConfiguration, configurationUnitType, message);
    }

    public static InvalidPropertyConfigurationProblem missingFieldProblem(final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(domainConfiguration, configurationUnitType, format("Missing property '%s' defined!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem rendererNotDefinedForFieldProblem(final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(domainConfiguration, configurationUnitType, format("Renderer not defined for custom property '%s'!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem notSupportedTypeFieldProblem(final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(domainConfiguration, configurationUnitType, format("Persistent property '%s' has not supported type!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem missingBaseDirectoryInFileReferenceProblem(final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(domainConfiguration, configurationUnitType, format("@FileReference property '%s' has incorrect baseDirectory defined!", propertyName));
    }

    public static InvalidPropertyConfigurationProblem invalidPropertyValueExpressionProblem(final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType, String propertyName) {
        return new InvalidPropertyConfigurationProblem(domainConfiguration, configurationUnitType, format("Property '%s' has invalid path/expression defined!", propertyName));
    }
}