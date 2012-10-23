package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.config.beans.parsing.configuration.DomainConfiguration;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.reporting.ProblemReporterLogImpl;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;
import static org.lightadmin.core.util.DomainConfigurationUtils.isConfigurationCandidate;

public class DomainConfigurationParser {

	private ProblemReporter problemReporter;

	private DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver;

	private DomainConfigurationValidator configurationValidator;

	private final Set<DomainConfigurationInterface> domainConfigurations = newLinkedHashSet();

	public DomainConfigurationParser( DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
		this.configurationValidator = new DomainConfigurationValidator( entityMetadataResolver );

		this.problemReporter = new ProblemReporterLogImpl();
	}

	public void parse( Set<Class> configurationClasses ) {
		for ( Class configurationClass : configurationClasses ) {
			if ( isConfigurationCandidate( configurationClass ) ) {
				final Class<?> domainType = configurationDomainType( configurationClass );
				processConfigurationClass( domainConfiguration( domainType, configurationClass ) );
			}
		}
	}

	private void processConfigurationClass( final DomainConfigurationInterface domainConfiguration ) {
		domainConfigurations.add( domainConfiguration );
	}

	public void validate() {
		for ( DomainConfigurationInterface configClass : this.domainConfigurations ) {
			configurationValidator.validate( configClass, problemReporter );
		}
	}

	private DomainTypeEntityMetadata domainEntityMetadata( final Class<?> domainType ) {
		return entityMetadataResolver.resolveEntityMetadata( domainType );
	}

	public Set<DomainConfigurationInterface> getDomainConfigurations() {
		final Set<DomainConfigurationInterface> result = newLinkedHashSet();
		for ( DomainConfigurationInterface domainConfiguration : domainConfigurations ) {
			result.add( decorateDomainConfiguration( domainConfiguration ) );
		}
		return result;
	}

	private DomainConfiguration domainConfiguration( final Class<?> domainType, final Class configurationClass ) {
		return new DomainConfiguration( domainEntityMetadata( domainType ), configurationClass );
	}

	private DomainConfigurationInterface decorateDomainConfiguration( final DomainConfigurationInterface domainConfiguration ) {
		return new DomainConfigurationDecorator( domainConfiguration, configurationValidator.validPropertyFilter( domainConfiguration ) );
	}
}