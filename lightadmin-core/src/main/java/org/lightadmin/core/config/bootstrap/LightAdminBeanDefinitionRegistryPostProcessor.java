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
package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.parsing.validation.CompositeConfigurationUnitsValidator;
import org.lightadmin.core.config.bootstrap.scanning.AdministrationClassScanner;
import org.lightadmin.core.config.bootstrap.scanning.ClassScanner;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.repository.DynamicRepositoryClassFactory;
import org.lightadmin.core.persistence.repository.JavassistDynamicJpaRepositoryClassFactory;
import org.lightadmin.core.util.DynamicRepositoryBeanNameGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.servlet.ServletContext;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromAutowiredConfiguration;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

public class LightAdminBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    public static final String JPA_MAPPPING_CONTEXT_BEAN = "jpaMapppingContext";
    public static final String REPOSITORIES_BEAN = "repositories";
    public static final String LIGHTADMIN_CONFIGURATION_BEAN = "lightAdminConfiguration";
    public static final String CONFIGURATION_UNITS_VALIDATOR_BEAN = "configurationUnitsValidator";

    protected static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    protected DynamicRepositoryBeanNameGenerator nameGenerator = new DynamicRepositoryBeanNameGenerator();
    protected DynamicRepositoryClassFactory classFactory = new JavassistDynamicJpaRepositoryClassFactory(nameGenerator);
    protected ClassScanner classScanner = new AdministrationClassScanner();

    protected ServletContext servletContext;

    protected String basePackage;

    public LightAdminBeanDefinitionRegistryPostProcessor(String basePackage, ServletContext servletContext) {
        this.servletContext = servletContext;
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        WebApplicationContext rootContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        EntityManager entityManager = findEntityManager(rootContext);

        ResourceLoader resourceLoader = newResourceLoader(servletContext);

        Set<Class> administrationConfigs = scanPackageForAdministrationClasses();

        Set<ConfigurationUnits> configurationUnits = configurationUnits(rootContext, administrationConfigs);

        registry.registerBeanDefinition(JPA_MAPPPING_CONTEXT_BEAN, mappingContext(entityManager));

        registry.registerBeanDefinition(CONFIGURATION_UNITS_VALIDATOR_BEAN, configurationUnitsValidator(resourceLoader));

        for (Class<?> managedEntityType : managedEntities(entityManager)) {
            Class repoInterface = createDynamicRepositoryClass(managedEntityType, entityManager);
            registry.registerBeanDefinition(beanName(repoInterface), repositoryFactory(repoInterface, entityManager));
        }

        registerRepositoryEventListeners(configurationUnits, registry);

        registry.registerBeanDefinition(beanName(GlobalAdministrationConfiguration.class), globalAdministrationConfigurationFactoryBeanDefinition(configurationUnits));
        registry.registerBeanDefinition(beanName(DomainTypeAdministrationConfigurationFactory.class), domainTypeAdministrationConfigurationFactoryDefinition(entityManager));
    }

    private void registerRepositoryEventListeners(Set<ConfigurationUnits> configurationUnits, BeanDefinitionRegistry registry) {
        for (ConfigurationUnits configurationUnit : configurationUnits) {
            Class<? extends AbstractRepositoryEventListener> repositoryEventListenerClass = configurationUnit.getEntityConfiguration().getRepositoryEventListener();
            if (repositoryEventListenerClass != null) {
                registry.registerBeanDefinition(beanName(repositoryEventListenerClass), repositoryEventListener(repositoryEventListenerClass));
            }
        }
    }

    private Set<ConfigurationUnits> configurationUnits(WebApplicationContext rootContext, Set<Class> administrationConfigs) {
        Set<ConfigurationUnits> configurationUnitsCollection = newLinkedHashSet();
        for (Class administrationConfig : administrationConfigs) {
            ConfigurationUnits configurationUnits = unitsFromAutowiredConfiguration(administrationConfig, rootContext.getAutowireCapableBeanFactory());
            configurationUnitsCollection.add(configurationUnits);
        }
        return configurationUnitsCollection;
    }

    private Iterable<Class<?>> managedEntities(EntityManager entityManager) {
        Set<Class<?>> managedEntities = newHashSet();
        for (EntityType<?> entity : entityManager.getMetamodel().getEntities()) {
            if (entity.getJavaType() != null)
                managedEntities.add(entity.getJavaType());
        }
        return managedEntities;
    }

    private BeanDefinition repositoryEventListener(Class<? extends AbstractRepositoryEventListener> repositoryEventListener) {
        BeanDefinitionBuilder builder = rootBeanDefinition(repositoryEventListener);
        builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_AUTODETECT);
        return builder.getBeanDefinition();
    }

    private BeanDefinition configurationUnitsValidator(ResourceLoader resourceLoader) {
        BeanDefinitionBuilder builder = rootBeanDefinition(CompositeConfigurationUnitsValidator.class);
        builder.addConstructorArgReference(JPA_MAPPPING_CONTEXT_BEAN);
        builder.addConstructorArgValue(resourceLoader);
        builder.addConstructorArgReference(LIGHTADMIN_CONFIGURATION_BEAN);
        return builder.getBeanDefinition();
    }

    private BeanDefinition mappingContext(EntityManager entityManager) {
        BeanDefinitionBuilder builder = rootBeanDefinition(JpaMetamodelMappingContextFactoryBean.class);
        builder.addPropertyValue("entityManager", entityManager);
        return builder.getBeanDefinition();
    }

    private BeanDefinition globalAdministrationConfigurationFactoryBeanDefinition(Set<ConfigurationUnits> configurationUnits) {
        BeanDefinitionBuilder builder = rootBeanDefinition(GlobalAdministrationConfigurationFactoryBean.class);
        builder.addPropertyReference("domainTypeAdministrationConfigurationFactory", beanName(DomainTypeAdministrationConfigurationFactory.class));
        builder.addPropertyValue("domainTypeConfigurationUnits", configurationUnits);
        builder.addPropertyReference("mappingContext", JPA_MAPPPING_CONTEXT_BEAN);
        builder.addPropertyReference("repositories", REPOSITORIES_BEAN);
        builder.addPropertyReference("configurationUnitsValidator", CONFIGURATION_UNITS_VALIDATOR_BEAN);
        return builder.getBeanDefinition();
    }

    private BeanDefinition repositoryFactory(Class<?> repoInterface, EntityManager entityManager) {
        BeanDefinitionBuilder builder = rootBeanDefinition(JpaRepositoryFactoryBean.class);
        builder.addPropertyValue("entityManager", entityManager);
        builder.addPropertyReference("mappingContext", JPA_MAPPPING_CONTEXT_BEAN);
        builder.addPropertyValue("repositoryInterface", repoInterface);
        return builder.getBeanDefinition();
    }

    private BeanDefinition domainTypeAdministrationConfigurationFactoryDefinition(EntityManager entityManager) {
        BeanDefinitionBuilder builder = rootBeanDefinition(DomainTypeAdministrationConfigurationFactory.class);
        builder.addConstructorArgReference(REPOSITORIES_BEAN);
        builder.addConstructorArgValue(entityManager);
        builder.addConstructorArgReference(JPA_MAPPPING_CONTEXT_BEAN);
        return builder.getBeanDefinition();
    }

    private EntityManager findEntityManager(WebApplicationContext rootContext) {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryUtils.findEntityManagerFactory(rootContext, null);

        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

    private ServletContextResourceLoader newResourceLoader(ServletContext servletContext) {
        return new ServletContextResourceLoader(servletContext);
    }

    private Class createDynamicRepositoryClass(Class domainType, EntityManager entityManager) {
        EntityType entityType = entityManager.getMetamodel().entity(domainType);
        Class idType = entityType.getIdType().getJavaType();

        return classFactory.createDynamicRepositoryClass(domainType, idType);
    }

    private Set<Class> scanPackageForAdministrationClasses() {
        final Set<Class> administrationConfigs = newLinkedHashSet();
        for (String configurationsBasePackage : configurationsBasePackages()) {
            administrationConfigs.addAll(classScanner.scan(configurationsBasePackage));
        }

        return administrationConfigs;
    }

    private String[] configurationsBasePackages() {
        return tokenizeToStringArray(basePackage, CONFIG_LOCATION_DELIMITERS);
    }

    private String beanName(Class type) {
        return nameGenerator.generateBeanNameDecapitalized(type);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }
}