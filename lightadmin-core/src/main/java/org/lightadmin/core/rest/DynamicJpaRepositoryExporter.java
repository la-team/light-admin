package org.lightadmin.core.rest;

import org.lightadmin.core.config.support.BeanNameGenerator;
import org.lightadmin.core.persistence.repository.support.DynamicRepositoriesDecorator;
import org.springframework.data.rest.repository.jpa.JpaRepositoryExporter;

import static com.google.common.collect.Maps.newHashMap;

public class DynamicJpaRepositoryExporter extends JpaRepositoryExporter {

	@Override
	public void refresh() {
		if ( repositoriesAlreadyInitialized() ) {
			return;
		}

		repositories = new DynamicRepositoriesDecorator( applicationContext );

		repositoryMetadata = newHashMap();

		for ( Class<?> domainType : repositories ) {
			if ( exportOnlyTheseClasses.isEmpty() || exportOnlyTheseClasses.contains( domainType.getName() ) ) {
				final String repositoryServiceExporterName = BeanNameGenerator.INSTANCE.repositoryServiceExporterName( domainType );
				repositoryMetadata.put( repositoryServiceExporterName, createRepositoryMetadata( repositoryServiceExporterName, domainType, repositoryInterface( domainType ), repositories ) );
			}
		}
	}

	private boolean repositoriesAlreadyInitialized() {
		return null != repositories;
	}

	private Class<?> repositoryInterface( final Class<?> domainType ) {
		return repositories.getRepositoryInformationFor( domainType ).getRepositoryInterface();
	}
}