package org.lightadmin.core.extension;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;

public class JpaRepositoryFactoryBeanCreator implements RepositoryFactoryBeanCreator {

    private final DynamicJpaRepositoryClassFactory dynamicJpaRepositoryClassFactory;

    private final EntityManager entityManager;
    private final BeanFactory beanFactory;
    private final ClassLoader classLoader;

    public JpaRepositoryFactoryBeanCreator(DynamicJpaRepositoryClassFactory dynamicJpaRepositoryClassFactory, EntityManager entityManager, BeanFactory beanFactory) {
        this.entityManager = entityManager;
        this.classLoader = ClassUtils.getDefaultClassLoader();
        this.beanFactory = beanFactory;

        this.dynamicJpaRepositoryClassFactory = dynamicJpaRepositoryClassFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JpaRepositoryFactoryBean createRepositoryFactoryBean(final Class<?> domainType) {
        final Class idType = jpaEntityInformation(domainType).getIdType();

        final JpaRepositoryFactoryBean jpaRepositoryFactoryBean = new JpaRepositoryFactoryBean();
        jpaRepositoryFactoryBean.setEntityManager(this.entityManager);
        jpaRepositoryFactoryBean.setBeanClassLoader(this.classLoader);
        jpaRepositoryFactoryBean.setBeanFactory(this.beanFactory);
        jpaRepositoryFactoryBean.setRepositoryInterface(dynamicJpaRepositoryClassFactory.createDynamicRepositoryClass(domainType, idType));

        jpaRepositoryFactoryBean.afterPropertiesSet();

        return jpaRepositoryFactoryBean;
    }

    private JpaEntityInformation jpaEntityInformation(Class<?> domainType) {
        return ((JpaEntityInformation) JpaEntityInformationSupport.getMetadata(domainType, entityManager));
    }
}