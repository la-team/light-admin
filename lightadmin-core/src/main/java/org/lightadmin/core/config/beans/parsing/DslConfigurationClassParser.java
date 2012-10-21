package org.lightadmin.core.config.beans.parsing;

import com.google.common.collect.Sets;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.reporting.ProblemReporterLogImpl;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.util.ConfigurationClassUtils.configurationDomainType;
import static org.lightadmin.core.util.ConfigurationClassUtils.isConfigurationCandidate;

public class DslConfigurationClassParser {

	private ProblemReporter problemReporter = new ProblemReporterLogImpl();

	private final Set<DslConfigurationClass> dslConfigurations = newLinkedHashSet();

	public DslConfigurationClassParser() {
	}

	public void parse( Class... configurationClasses ) {
		parse( Sets.newHashSet( configurationClasses ) );
	}

	public void parse( Set<Class> configurationClasses ) {
		for ( Class configurationClass : configurationClasses ) {
			if ( isConfigurationCandidate( configurationClass ) ) {
				processConfigurationClass( new DslConfigurationClass( configurationDomainType( configurationClass ), configurationClass ) );
			}
		}
	}

	private void processConfigurationClass( final DslConfigurationClass dslConfigurationClass ) {
		dslConfigurations.add( dslConfigurationClass );
	}

	public void validate() {
		for ( DslConfigurationClass configClass : this.dslConfigurations ) {
			configClass.validate( problemReporter );
		}
	}

	public Set<DslConfigurationClass> getDslConfigurations() {
		return newLinkedHashSet( dslConfigurations );
	}

	public void setProblemReporter( final ProblemReporter problemReporter ) {
		this.problemReporter = problemReporter;
	}
}