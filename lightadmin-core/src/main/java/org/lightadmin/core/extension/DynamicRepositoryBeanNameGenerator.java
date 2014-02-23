package org.lightadmin.core.extension;

import org.springframework.util.ClassUtils;

import static java.beans.Introspector.decapitalize;

public class DynamicRepositoryBeanNameGenerator {

    public String generateBeanNameDecapitalized(Class<?> domainType, Class<?> repositoryInterface) {
        return decapitalize(generateBeanName(domainType, repositoryInterface));
    }

    public String generateBeanName(Class<?> domainType, Class<?> repositoryInterface) {
        String repositoryInterfaceClassName = ClassUtils.getShortName(repositoryInterface);
        String domainTypeClassName = ClassUtils.getShortName(domainType);

        return domainTypeClassName + repositoryInterfaceClassName;
    }
}