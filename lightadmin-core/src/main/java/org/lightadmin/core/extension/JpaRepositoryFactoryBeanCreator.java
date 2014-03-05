package org.lightadmin.core.extension;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;

public class JpaRepositoryFactoryBeanCreator implements RepositoryFactoryBeanCreator {

    private final DynamicRepositoryClassFactory dynamicRepositoryClassFactory;

    private final EntityManager entityManager;
    private final BeanFactory beanFactory;
    private final ClassLoader classLoader;

    public JpaRepositoryFactoryBeanCreator(DynamicRepositoryClassFactory dynamicRepositoryClassFactory, EntityManager entityManager, BeanFactory beanFactory) {
        this.entityManager = entityManager;
        this.beanFactory = beanFactory;
        this.dynamicRepositoryClassFactory = dynamicRepositoryClassFactory;

        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    @Override
    @SuppressWarnings("unchecked")
    public JpaRepositoryFactoryBean createRepositoryFactoryBean(final Class<?> domainType) {
        final Class idType = jpaEntityInformation(domainType).getIdType();

        final JpaRepositoryFactoryBean jpaRepositoryFactoryBean = new JpaRepositoryFactoryBean();
        jpaRepositoryFactoryBean.setEntityManager(this.entityManager);
        jpaRepositoryFactoryBean.setBeanClassLoader(this.classLoader);
        jpaRepositoryFactoryBean.setBeanFactory(this.beanFactory);
        jpaRepositoryFactoryBean.setRepositoryInterface(this.dynamicRepositoryClassFactory.createDynamicRepositoryClass(domainType, idType));

        jpaRepositoryFactoryBean.afterPropertiesSet();

        return jpaRepositoryFactoryBean;
    }

    private JpaEntityInformation jpaEntityInformation(Class<?> domainType) {
        return ((JpaEntityInformation) JpaEntityInformationSupport.getMetadata(domainType, entityManager));
    }
}