package org.lightadmin.core.extension;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AnnotationRepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
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

        final JpaRepositoryFactoryBean jpaRepositoryFactoryBean = new EnhancedJpaRepositoryFactoryBean();
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

    private static class EnhancedJpaRepositoryFactoryBean extends JpaRepositoryFactoryBean {
        @Override
        protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
            return new EnhancedJpaRepositoryFactory(entityManager);
        }
    }

    private static class EnhancedJpaRepositoryFactory extends JpaRepositoryFactory {

        public EnhancedJpaRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }

        RepositoryMetadata getRepositoryMetadata(Class<?> repositoryInterface) {
            return Repository.class.isAssignableFrom(repositoryInterface) ? new DefaultRepositoryMetadata(repositoryInterface)
                    : new AnnotationRepositoryMetadata(repositoryInterface);
        }
    }
}