package org.lightadmin.core.util;

import org.springframework.util.ClassUtils;

import static java.beans.Introspector.decapitalize;

public class DynamicRepositoryBeanNameGenerator {

    public String generateBeanNameDecapitalized(Class<?> clazz) {
        return decapitalize(ClassUtils.getShortName(clazz));
    }

    public String generateBeanName(Class<?> domainType, Class<?> repositoryInterface) {
        String repositoryInterfaceClassName = ClassUtils.getShortName(repositoryInterface);
        String domainTypeClassName = ClassUtils.getShortName(domainType);

        return domainTypeClassName + repositoryInterfaceClassName;
    }
}