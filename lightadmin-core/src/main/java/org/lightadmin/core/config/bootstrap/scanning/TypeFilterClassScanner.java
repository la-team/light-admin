package org.lightadmin.core.config.bootstrap.scanning;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ClassUtils.resolveClassName;

public class TypeFilterClassScanner implements ClassScanner {

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    private final TypeFilter[] typeFilters;

    public TypeFilterClassScanner(TypeFilter... typeFilters) {
        this.typeFilters = typeFilters;
    }

    @Override
    public Set<Class> scan(String basePackage) {
        final Set<Class> classes = newLinkedHashSet();

        try {
            final Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath(basePackage));
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    if (hasApplicableType(metadataReader)) {
                        String className = metadataReader.getClassMetadata().getClassName();
                        Class<?> administrationClass = resolveClassName(className, this.resourcePatternResolver.getClassLoader());

                        classes.add(administrationClass);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("I/O failure during package scanning", ex);
        }

        return classes;

    }

    private boolean hasApplicableType(MetadataReader metadataReader) throws IOException {
        for (TypeFilter typeFilter : typeFilters) {
            if (typeFilter.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

    private String packageSearchPath(final String basePackage) {
        return CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
    }

    private String resolveBasePackage(String basePackage) {
        return convertClassNameToResourcePath(basePackage);
    }
}