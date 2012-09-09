package org.lightadmin.demo;

import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.repository.annotation.RestResource;
import org.springframework.data.rest.repository.jpa.JpaRepositoryExporter;
import org.springframework.data.rest.repository.jpa.JpaRepositoryMetadata;
import org.springframework.util.StringUtils;

import java.util.HashMap;

// TODO: max: Kill this later!
public class DynamicJpaRepositoryExporter extends JpaRepositoryExporter {

	public void refresh() {
		if ( null != repositories ) {
			return;
		}
		repositories = new Repositories( applicationContext );
		repositoryMetadata = new HashMap<String, JpaRepositoryMetadata>();
		for ( Class<?> domainType : repositories ) {
			if ( exportOnlyTheseClasses.isEmpty() || exportOnlyTheseClasses.contains( domainType.getName() ) ) {
				Class<?> repoClass = repositories.getRepositoryInformationFor( domainType ).getRepositoryInterface();
				String name = StringUtils.uncapitalize( domainType.getSimpleName() );
				RestResource resourceAnno = repoClass.getAnnotation( RestResource.class );
				boolean exported = true;
				if ( null != resourceAnno ) {
					if ( StringUtils.hasText( resourceAnno.path() ) ) {
						name = resourceAnno.path();
					}
					exported = resourceAnno.exported();
				}
				if ( exported ) {
					repositoryMetadata.put( name, createRepositoryMetadata( name, domainType, repoClass, repositories ) );
				}
			}
		}
	}
}