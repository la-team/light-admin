package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.reporting.ProblemReporter;

public interface ConfigurationUnitsValidator<T extends ConfigurationUnits> {

    void validate(T configurationUnits, ProblemReporter problemReporter);

}