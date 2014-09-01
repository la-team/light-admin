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
package org.lightadmin.core.util;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static org.springframework.beans.BeanUtils.instantiateClass;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.ClassUtils.getConstructorIfAvailable;
import static org.springframework.util.ClassUtils.getMethodIfAvailable;
import static org.springframework.util.ReflectionUtils.invokeMethod;

public abstract class DomainConfigurationUtils {

    public static Class<?> configurationDomainType(Class clazz) {
        if (isAnnotationBasedConfigurationCandidate(clazz)) {
            final Administration annotation = findAnnotation(clazz, Administration.class);

            return annotation == null ? null : annotation.value();
        }

        if (isSuperClassBasedConfigurationCandidate(clazz)) {
            final ParameterizedType genericSuperclass = (ParameterizedType) clazz.getGenericSuperclass();

            return (Class<?>) genericSuperclass.getActualTypeArguments()[0];
        }

        throw new RuntimeException("Unknown configuration candidate");
    }

    public static boolean isAnnotationBasedConfigurationCandidate(Class clazz) {
        return hasAdministrationAnnotation(clazz);
    }

    public static boolean isSuperClassBasedConfigurationCandidate(Class clazz) {
        return ClassUtils.isAssignable(AdministrationConfiguration.class, clazz);
    }

    private static boolean hasAdministrationAnnotation(Class clazz) {
        return findAnnotation(clazz, Administration.class) != null;
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends ConfigurationUnit> T initializeConfigurationUnitWithBuilder(final AdministrationConfiguration configurationInstance, DomainConfigurationUnitType configurationUnitType, Class<? extends ConfigurationUnitBuilder<T>> builderInterface, Class<? extends ConfigurationUnitBuilder<T>> concreteBuilderClass) {
        final Class<? extends AdministrationConfiguration> configurationClass = configurationInstance.getClass();
        final Class<?> domainType = configurationDomainType(configurationClass);

        final Method method = getMethodIfAvailable(configurationClass, configurationUnitType.getName(), builderInterface);

        final ConfigurationUnitBuilder<T> builder = instantiateBuilder(concreteBuilderClass, domainType, configurationUnitType);

        try {
            return (T) invokeMethod(method, configurationInstance, builder);
        } catch (Exception ex) {
            return instantiateBuilder(concreteBuilderClass, domainType, configurationUnitType).build();
        }
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends ConfigurationUnit> T initializeConfigurationUnitWithBuilder(final Class<?> configurationClass, DomainConfigurationUnitType configurationUnitType, Class<? extends ConfigurationUnitBuilder<T>> builderInterface, Class<? extends ConfigurationUnitBuilder<T>> concreteBuilderClass) {
        final Class<?> domainType = configurationDomainType(configurationClass);

        final Method method = getMethodIfAvailable(configurationClass, configurationUnitType.getName(), builderInterface);

        final ConfigurationUnitBuilder<T> builder = instantiateBuilder(concreteBuilderClass, domainType, configurationUnitType);
        if (method != null) {
            try {
                return (T) invokeMethod(method, null, builder);
            } catch (Exception ex) {
                return instantiateBuilder(concreteBuilderClass, domainType, configurationUnitType).build();
            }
        }

        return builder.build();
    }

    private static <T extends ConfigurationUnit> ConfigurationUnitBuilder<T> instantiateBuilder(final Class<? extends ConfigurationUnitBuilder<T>> concreteBuilderClass, final Class domainType, DomainConfigurationUnitType configurationUnitType) {
        Constructor<? extends ConfigurationUnitBuilder<T>> extendedConstructor = getConstructorIfAvailable(concreteBuilderClass, Class.class, DomainConfigurationUnitType.class);

        if (extendedConstructor != null) {
            return instantiateClass(extendedConstructor, domainType, configurationUnitType);
        }

        Constructor<? extends ConfigurationUnitBuilder<T>> constructor = getConstructorIfAvailable(concreteBuilderClass, Class.class);
        if (constructor != null) {
            return instantiateClass(constructor, domainType);
        }

        return instantiateClass(concreteBuilderClass);
    }
}