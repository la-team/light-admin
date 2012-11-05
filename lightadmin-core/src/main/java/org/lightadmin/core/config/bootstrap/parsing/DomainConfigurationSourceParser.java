package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Set;

public interface DomainConfigurationSourceParser<T> {

	void parse( Set<T> domainConfigurationSources );

	void validate( ProblemReporter problemReporter );

	Set<DomainConfigurationSource<T>> getDomainConfigurationSources();
}