package org.lightadmin.core.rest;

import org.lightadmin.core.repository.support.DynamicRepositoriesDecorator;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.rest.repository.annotation.RestResource;
import org.springframework.data.rest.repository.jpa.JpaRepositoryExporter;
import org.springframework.data.rest.repository.jpa.JpaRepositoryMetadata;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

// TODO: max: Kill this later!
public class DynamicJpaRepositoryExporter extends JpaRepositoryExporter {

	public void refresh() {
		if ( null != repositories ) {
			return;
		}
		repositories = new DynamicRepositoriesDecorator( applicationContext );
		repositoryMetadata = new HashMap<String, JpaRepositoryMetadata>();
		for ( Class<?> domainType : repositories ) {
			if ( exportOnlyTheseClasses.isEmpty() || exportOnlyTheseClasses.contains( domainType.getName() ) ) {
				final RepositoryInformation repositoryInformation = repositories.getRepositoryInformationFor( domainType );
				final JpaEntityInformation<Object,Serializable> entityInformation = (JpaEntityInformation<Object,Serializable>) repositories.getEntityInformationFor( domainType );

				Class<?> repoClass = repositoryInformation.getRepositoryInterface();
				String entityName = StringUtils.uncapitalize( entityInformation.getEntityName() );

				RestResource resourceAnno = repoClass.getAnnotation( RestResource.class );
				boolean exported = true;
				if ( null != resourceAnno ) {
					if ( StringUtils.hasText( resourceAnno.path() ) ) {
						entityName = resourceAnno.path();
					}
					exported = resourceAnno.exported();
				}
				if ( exported ) {
					repositoryMetadata.put( entityName, createRepositoryMetadata( entityName, domainType, repoClass, repositories ) );
				}
			}
		}
	}
}