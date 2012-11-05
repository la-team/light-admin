package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Set;

public interface DomainTypeAdministrationConfigurationReader<S> {

	Set<DomainTypeAdministrationConfiguration> loadDomainTypeConfiguration( Set<S> source );

	Set<DomainTypeAdministrationConfiguration> loadDomainTypeConfiguration( Set<S> source, ProblemReporter problemReporter );
}