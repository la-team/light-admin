/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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