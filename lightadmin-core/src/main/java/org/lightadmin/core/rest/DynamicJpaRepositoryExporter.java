package org.lightadmin.core.rest;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.repository.support.DynamicRepositoriesDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.repository.jpa.JpaRepositoryExporter;
import org.springframework.util.StringUtils;

import static com.google.common.collect.Maps.newHashMap;

public class DynamicJpaRepositoryExporter extends JpaRepositoryExporter {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Autowired
	private DomainTypeEntityMetadataResolver domainTypeEntityMetadataResolver;

	@Override
	public void refresh() {
		//		if ( repositoriesAlreadyInitialized() ) {
		//			return;
		//		}

		repositories = new DynamicRepositoriesDecorator( globalAdministrationConfiguration, domainTypeEntityMetadataResolver );

		repositoryMetadata = newHashMap();

		for ( Class<?> domainType : repositories ) {
			if ( exportOnlyTheseClasses.isEmpty() || exportOnlyTheseClasses.contains( domainType.getName() ) ) {
				final String repositoryServiceExporterName = repositoryServiceExporterName( domainType );

				repositoryMetadata.put( repositoryServiceExporterName, createRepositoryMetadata( repositoryServiceExporterName, domainType, repositoryInterface( domainType ), repositories ) );
			}
		}
	}

	private String repositoryServiceExporterName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() );
	}

	private Class<?> repositoryInterface( final Class<?> domainType ) {
		return repositories.getRepositoryInformationFor( domainType ).getRepositoryInterface();
	}

	private boolean repositoriesAlreadyInitialized() {
		return null != repositories;
	}
}