package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.reporting.ProblemReporter;

public interface DomainConfigurationSourceValidator<T extends DomainConfigurationSource> {

	void validate( T domainConfigurationSource, ProblemReporter problemReporter );

}