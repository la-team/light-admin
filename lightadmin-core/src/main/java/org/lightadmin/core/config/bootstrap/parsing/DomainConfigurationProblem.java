package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.reporting.Problem;

@SuppressWarnings("unused")
public class DomainConfigurationProblem extends Problem {

    private final ConfigurationUnits configurationUnits;

    private DomainConfigurationUnitType configurationUnitType;

    public DomainConfigurationProblem(final ConfigurationUnits configurationUnits, final String message) {
        super(message);
        this.configurationUnits = configurationUnits;
    }

    public DomainConfigurationProblem(final ConfigurationUnits configurationUnits, DomainConfigurationUnitType configurationUnitType, final String message) {
        this(configurationUnits, message);
        this.configurationUnitType = configurationUnitType;
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

    @Override
    public String toString() {
        return getMessage();
    }

    public ConfigurationUnits getConfigurationUnits() {
        return configurationUnits;
    }

    public DomainConfigurationUnitType getConfigurationUnitType() {
        return configurationUnitType;
    }
}