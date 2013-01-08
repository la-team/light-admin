package org.lightadmin.core.config.bootstrap.scanning;

import org.lightadmin.core.config.annotation.Administration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class AdministrationClassScanner implements ClassScanner {

	private static final AnnotationTypeFilter ADMINISTRATION_TYPE_FILTER = new AnnotationTypeFilter( Administration.class );

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory( this.resourcePatternResolver );

	@Override
	public Set<Class> scan( String basePackage ) {
		final Set<Class> configurations = newLinkedHashSet();

		try {
			Resource[] resources = this.resourcePatternResolver.getResources( packageSearchPath( basePackage ) );
			for ( Resource resource : resources ) {
				if ( resource.isReadable() ) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader( resource );
					if ( isAdministrationClass( metadataReader ) ) {
						String className = metadataReader.getClassMetadata().getClassName();
						final Class<?> administrationClass = ClassUtils.resolveClassName( className, this.resourcePatternResolver.getClassLoader() );
						configurations.add( administrationClass );
					}
				}
			}
		} catch ( IOException ex ) {
			throw new RuntimeException( "I/O failure during package scanning", ex );
		}

		return configurations;
	}

	private String packageSearchPath( final String basePackage ) {
		return ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage( basePackage ) + "/" + DEFAULT_RESOURCE_PATTERN;
	}

	private String resolveBasePackage( String basePackage ) {
		return ClassUtils.convertClassNameToResourcePath( basePackage );
	}

	private boolean isAdministrationClass( MetadataReader metadataReader ) throws IOException {
		return ADMINISTRATION_TYPE_FILTER.match( metadataReader, this.metadataReaderFactory );
	}
}