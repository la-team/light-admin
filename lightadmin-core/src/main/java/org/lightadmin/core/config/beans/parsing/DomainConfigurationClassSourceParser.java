package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationClassSource;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.beans.parsing.validation.DomainConfigurationClassSourceValidator;
import org.lightadmin.core.config.beans.parsing.validation.DomainConfigurationSourceValidator;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;
import static org.lightadmin.core.util.DomainConfigurationUtils.isConfigurationCandidate;

public class DomainConfigurationClassSourceParser implements DomainConfigurationSourceParser<Class> {

	private DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver;

	private DomainConfigurationSourceValidator<DomainConfigurationSource<Class>> configurationClassSourceValidator;

	private final Set<DomainConfigurationSource<Class>> domainConfigurations = newLinkedHashSet();

	public DomainConfigurationClassSourceParser( DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;

		this.configurationClassSourceValidator = new DomainConfigurationClassSourceValidator( entityMetadataResolver );
	}

	public void parse( Set<Class> configurationClasses ) {
		for ( Class configurationClass : configurationClasses ) {
			if ( isConfigurationCandidate( configurationClass ) ) {
				final Class<?> domainType = configurationDomainType( configurationClass );
				domainConfigurations.add( domainConfiguration( domainType, configurationClass ) );
			}
		}
	}

	public void validate( ProblemReporter problemReporter ) {
		for ( DomainConfigurationSource<Class> domainConfiguration : domainConfigurations ) {
			configurationClassSourceValidator.validate( domainConfiguration, problemReporter );
		}
	}

	@Override
	public Set<DomainConfigurationSource<Class>> getDomainConfigurationSources() {
		return domainConfigurations;
	}

	private DomainTypeEntityMetadata domainEntityMetadata( final Class<?> domainType ) {
		return entityMetadataResolver.resolveEntityMetadata( domainType );
	}

	private DomainConfigurationClassSource domainConfiguration( final Class<?> domainType, final Class configurationClass ) {
		return new DomainConfigurationClassSource( domainEntityMetadata( domainType ), configurationClass );
	}
}