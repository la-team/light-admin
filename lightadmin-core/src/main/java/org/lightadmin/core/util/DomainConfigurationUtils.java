package org.lightadmin.core.util;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
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

    public static boolean isConfigurationCandidate(Class clazz) {
        return isAnnotationBasedConfigurationCandidate(clazz) || isSuperClassBasedConfigurationCandidate(clazz);
    }

    private static boolean hasAdministrationAnnotation(Class clazz) {
        return findAnnotation(clazz, Administration.class) != null;
    }

    public static boolean isConfigurationCandidate(Object candidate) {
        return ClassUtils.isAssignableValue(Class.class, candidate) && isConfigurationCandidate((Class) candidate);
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends ConfigurationUnit> T initializeConfigurationUnitWithBuilder(final AdministrationConfiguration configurationInstance, DomainConfigurationUnitType configurationUnitType, Class<? extends ConfigurationUnitBuilder<T>> builderInterface, Class<? extends ConfigurationUnitBuilder<T>> concreteBuilderClass) {
        final Class<? extends AdministrationConfiguration> configurationClass = configurationInstance.getClass();
        final Class<?> domainType = configurationDomainType(configurationClass);

        final Method method = ClassUtils.getMethodIfAvailable(configurationClass, configurationUnitType.getName(), builderInterface);

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

        final Method method = ClassUtils.getMethodIfAvailable(configurationClass, configurationUnitType.getName(), builderInterface);

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
        Constructor<? extends ConfigurationUnitBuilder<T>> extendedConstructor = ClassUtils.getConstructorIfAvailable(concreteBuilderClass, Class.class, DomainConfigurationUnitType.class);

        if (extendedConstructor != null) {
            return BeanUtils.instantiateClass(extendedConstructor, domainType, configurationUnitType);
        }

        Constructor<? extends ConfigurationUnitBuilder<T>> constructor = ClassUtils.getConstructorIfAvailable(concreteBuilderClass, Class.class);
        if (constructor != null) {
            return BeanUtils.instantiateClass(constructor, domainType);
        }

        return BeanUtils.instantiateClass(concreteBuilderClass);
    }
}