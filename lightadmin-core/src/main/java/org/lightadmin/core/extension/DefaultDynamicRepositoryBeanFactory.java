package org.lightadmin.core.extension;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultDynamicRepositoryBeanFactory implements DynamicRepositoryBeanFactory, ApplicationEventPublisherAware, ApplicationListener<AdministrationConfigurationRefreshedEvent> {

    private static final String DELEGATE_BEANS_FIELD = "beans";

    private final DynamicRepositoryBeanNameGenerator beanNameGenerator;
    private final RepositoryFactoryBeanCreator repositoryFactoryBeanCreator;
    private ApplicationEventPublisher applicationEventPublisher;

    private final StaticListableBeanFactory beanFactoryDelegate;

    public DefaultDynamicRepositoryBeanFactory(DynamicRepositoryBeanNameGenerator beanNameGenerator, RepositoryFactoryBeanCreator repositoryFactoryBeanCreator) {
        this.beanNameGenerator = beanNameGenerator;
        this.repositoryFactoryBeanCreator = repositoryFactoryBeanCreator;
        this.beanFactoryDelegate = new StaticListableBeanFactory();
    }

    @Override
    public void onApplicationEvent(AdministrationConfigurationRefreshedEvent event) {
        emptyBeansCache();

        populateBeansCache(event.getGlobalAdministrationConfiguration());
    }

    private void populateBeansCache(GlobalAdministrationConfiguration administrationConfiguration) {
        final Set<Class<?>> domainTypes = administrationConfiguration.getAllDomainTypes();
        for (Class<?> domainType : domainTypes) {
            addRepositoryFactoryBean(domainType);
        }

        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(new DynamicRepositoryBeanFactoryRefreshedEvent(this));
        }
    }

    private void addRepositoryFactoryBean(Class<?> domainType) {
        final RepositoryFactoryInformation repositoryFactoryBean = repositoryFactoryBeanCreator.createRepositoryFactoryBean(domainType);

        RepositoryInformation repositoryInformation = repositoryFactoryBean.getRepositoryInformation();
        Class<?> repositoryInterface = repositoryInformation.getRepositoryInterface();

        final String beanName = beanNameGenerator.generateBeanNameDecapitalized(repositoryInterface);

        addBean(beanName, repositoryFactoryBean);
    }

    private void addBean(String name, RepositoryFactoryInformation repositoryFactoryInformation) {
        beanFactoryDelegate.addBean(name, repositoryFactoryInformation);
    }

    private void emptyBeansCache() {
        BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(beanFactoryDelegate);
        beanWrapper.setPropertyValue(DELEGATE_BEANS_FIELD, new HashMap<String, Object>());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> beans() {
        BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(beanFactoryDelegate);
        return (Map<String, Object>) beanWrapper.getPropertyValue(DELEGATE_BEANS_FIELD);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return beanFactoryDelegate.getBean(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) beans().get(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return beanFactoryDelegate.getBean(requiredType);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return beanFactoryDelegate.getBean(name, args);
    }

    @Override
    public boolean containsBean(String name) {
        return beanFactoryDelegate.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactoryDelegate.isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return beanFactoryDelegate.isPrototype(name);
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException {
        return beanFactoryDelegate.isTypeMatch(name, targetType);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return beanFactoryDelegate.getType(name);
    }

    @Override
    public String[] getAliases(String name) {
        return beanFactoryDelegate.getAliases(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return beanFactoryDelegate.containsBeanDefinition(name);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanFactoryDelegate.getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactoryDelegate.getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return beanFactoryDelegate.getBeanNamesForType(type);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean includeFactoryBeans) {
        return beanFactoryDelegate.getBeanNamesForType(type, includeNonSingletons, includeFactoryBeans);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return beanFactoryDelegate.getBeansOfType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean includeFactoryBeans) throws BeansException {
        return beanFactoryDelegate.getBeansOfType(type, includeNonSingletons, includeFactoryBeans);
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
        return beanFactoryDelegate.getBeansWithAnnotation(annotationType);
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) {
        return beanFactoryDelegate.findAnnotationOnBean(beanName, annotationType);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}