package org.lightadmin.core.config.beans;

import org.lightadmin.core.config.beans.parsing.DomainConfigurationSourceParser;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.DynamicJpaRepositoryFactory;
import org.lightadmin.core.reporting.ProblemReporter;

import java.io.Serializable;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class SimpleDomainTypeAdministrationConfigurationReader implements DomainTypeAdministrationConfigurationReader<Class> {

	private final ProblemReporter problemReporter;

	private final DomainConfigurationSourceParser<Class> configurationSourceParser;

	private final DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory;

	public SimpleDomainTypeAdministrationConfigurationReader( final DomainConfigurationSourceParser<Class> configurationSourceParser, final DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory, final ProblemReporter problemReporter ) {
		this.problemReporter = problemReporter;

		this.configurationSourceParser = configurationSourceParser;
		this.dynamicJpaRepositoryFactory = dynamicJpaRepositoryFactory;
	}

	@Override
	public Set<DomainTypeAdministrationConfiguration> loadDomainTypeConfiguration( final Set<Class> source, final ProblemReporter problemReporter ) {
		configurationSourceParser.parse( source );

		configurationSourceParser.validate( problemReporter );

		return transform( configurationSourceParser.getDomainConfigurationSources() );
	}

	@Override
	public Set<DomainTypeAdministrationConfiguration> loadDomainTypeConfiguration( final Set<Class> source ) {
		return loadDomainTypeConfiguration( source, this.problemReporter );
	}

	private Set<DomainTypeAdministrationConfiguration> transform( final Set<DomainConfigurationSource<Class>> domainConfigurations ) {
		final Set<DomainTypeAdministrationConfiguration> domainTypeAdministrationConfigurations = newLinkedHashSet();
		for ( DomainConfigurationSource<Class> domainConfiguration : domainConfigurations ) {
			domainTypeAdministrationConfigurations.add( createConfiguration( domainConfiguration ) );
		}
		return domainTypeAdministrationConfigurations;
	}

	@SuppressWarnings( "unchecked" )
	private DomainTypeAdministrationConfiguration createConfiguration( DomainConfigurationSource<Class> domainConfiguration ) {
		final DynamicJpaRepository<?, ? extends Serializable> repository = dynamicJpaRepositoryFactory.createRepository( domainConfiguration.getDomainType() );

		DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = new DomainTypeAdministrationConfiguration( domainConfiguration.getDomainType(), repository );

		domainTypeAdministrationConfiguration.setDomainTypeEntityMetadata( domainConfiguration.getDomainTypeEntityMetadata() );
		domainTypeAdministrationConfiguration.setEntityConfiguration( domainConfiguration.getConfiguration() );
		domainTypeAdministrationConfiguration.setScreenContext( domainConfiguration.getScreenContext() );
		domainTypeAdministrationConfiguration.setListViewFragment( domainConfiguration.getListViewFragment() );
		domainTypeAdministrationConfiguration.setScopes( domainConfiguration.getScopes() );
		domainTypeAdministrationConfiguration.setFilters( domainConfiguration.getFilters() );

		return domainTypeAdministrationConfiguration;
	}
}