package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.reporting.Problem;

@SuppressWarnings("unused")
public class DomainConfigurationProblem extends Problem {

    private final DomainConfigurationSource domainConfiguration;

    private DomainConfigurationUnitType configurationUnitType;

    public DomainConfigurationProblem(final DomainConfigurationSource domainConfiguration, final String message) {
        super(message);
        this.domainConfiguration = domainConfiguration;
    }

    public DomainConfigurationProblem(final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType, final String message) {
        this(domainConfiguration, message);
        this.configurationUnitType = configurationUnitType;
    }

    @Override
    public String getMessage() {
        final StringBuilder stringBuilder = new StringBuilder("Domain Configuration ");
        stringBuilder.append("\"").append(domainConfiguration.getConfigurationName()).append("\"").append(": ");
        if (configurationUnitType != null) {
            stringBuilder.append("Unit ").append("\"").append(configurationUnitType.getName()).append("\"").append(": ");
        }
        stringBuilder.append(super.getMessage());

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public DomainConfigurationSource getDomainConfiguration() {
        return domainConfiguration;
    }

    public DomainConfigurationUnitType getConfigurationUnitType() {
        return configurationUnitType;
    }
}