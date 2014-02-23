package org.lightadmin.core.extension;

import org.springframework.util.ClassUtils;

import static java.beans.Introspector.decapitalize;

public class DynamicRepositoryBeanNameGenerator {

    public String generateBeanNameDecapitalized(Class<?> repositoryInterface) {
        String repositoryInterfaceClassName = ClassUtils.getShortName(repositoryInterface);

        return decapitalize(repositoryInterfaceClassName);
    }

    public String generateBeanName(Class<?> domainType, Class<?> repositoryInterface) {
        String repositoryInterfaceClassName = ClassUtils.getShortName(repositoryInterface);
        String domainTypeClassName = ClassUtils.getShortName(domainType);

        return domainTypeClassName + repositoryInterfaceClassName;
    }
}