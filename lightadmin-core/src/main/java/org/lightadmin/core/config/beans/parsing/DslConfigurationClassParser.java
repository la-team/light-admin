package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.reporting.ProblemReporterLogImpl;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.util.ConfigurationClassUtils.configurationDomainType;
import static org.lightadmin.core.util.ConfigurationClassUtils.isConfigurationCandidate;

public class DslConfigurationClassParser {

	private ProblemReporter problemReporter = new ProblemReporterLogImpl();

	private DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> domainTypeEntityMetadataResolver;

	private final Set<DslConfigurationClass> dslConfigurations = newLinkedHashSet();

	public DslConfigurationClassParser( DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> domainTypeEntityMetadataResolver ) {
		this.domainTypeEntityMetadataResolver = domainTypeEntityMetadataResolver;
	}

	public void parse( Set<Class> configurationClasses ) {
		for ( Class configurationClass : configurationClasses ) {
			if ( isConfigurationCandidate( configurationClass ) ) {
				final Class<?> domainType = configurationDomainType( configurationClass );
				processConfigurationClass( new DslConfigurationClass( domainEntityMetadata( domainType ), configurationClass ) );
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

	private DomainTypeEntityMetadata domainEntityMetadata( final Class<?> domainType ) {
		return domainTypeEntityMetadataResolver.resolveEntityMetadata( domainType );
	}

	public Set<DslConfigurationClass> getDslConfigurations() {
		return newLinkedHashSet( dslConfigurations );
	}

	public void setProblemReporter( final ProblemReporter problemReporter ) {
		this.problemReporter = problemReporter;
	}
}