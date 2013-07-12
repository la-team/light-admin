package org.lightadmin.core.config.bootstrap.scanning;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.core.config.annotation.Administration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.IOException;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ClassUtils.resolveClassName;

public class AdministrationClassScanner implements ClassScanner {

    private static final AssignableTypeFilter ADMINISTRATION_SUPERTYPE_TYPE_FILTER = new AssignableTypeFilter(AdministrationConfiguration.class);
    private static final AnnotationTypeFilter ADMINISTRATION_ANNOTATION_TYPE_FILTER = new AnnotationTypeFilter(Administration.class);

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    @Override
    public Set<Class> scan(String basePackage) {
        final Set<Class> configurations = newLinkedHashSet();

        try {
            final Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath(basePackage));
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    if (isAdministrationClass(metadataReader)) {
                        String className = metadataReader.getClassMetadata().getClassName();
                        final Class<?> administrationClass = resolveClassName(className, this.resourcePatternResolver.getClassLoader());
                        configurations.add(administrationClass);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("I/O failure during package scanning", ex);
        }

        return configurations;
    }

    private String packageSearchPath(final String basePackage) {
        return CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
    }

    private String resolveBasePackage(String basePackage) {
        return convertClassNameToResourcePath(basePackage);
    }

    private boolean isAdministrationClass(MetadataReader metadataReader) throws IOException {
        return ADMINISTRATION_SUPERTYPE_TYPE_FILTER.match(metadataReader, this.metadataReaderFactory) ||
                ADMINISTRATION_ANNOTATION_TYPE_FILTER.match(metadataReader, this.metadataReaderFactory);
    }
}