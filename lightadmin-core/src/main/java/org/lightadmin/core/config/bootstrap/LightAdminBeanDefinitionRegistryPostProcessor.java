package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.scanning.AdministrationClassScanner;
import org.lightadmin.core.config.bootstrap.scanning.ClassScanner;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.extension.DynamicRepositoryBeanNameGenerator;
import org.lightadmin.core.extension.DynamicRepositoryClassFactory;
import org.lightadmin.core.extension.JavassistDynamicJpaRepositoryClassFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.servlet.ServletContext;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromAutowiredConfiguration;
import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

public class LightAdminBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    protected static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    protected final DynamicRepositoryBeanNameGenerator nameGenerator = new DynamicRepositoryBeanNameGenerator();
    protected final DynamicRepositoryClassFactory classFactory = new JavassistDynamicJpaRepositoryClassFactory(nameGenerator);
    protected final ServletContext servletContext;
    protected final String basePackage;

    public LightAdminBeanDefinitionRegistryPostProcessor(String basePackage, ServletContext servletContext) {
        this.basePackage = basePackage;
        this.servletContext = servletContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        WebApplicationContext rootContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        EntityManagerFactory entityManagerFactory = EntityManagerFactoryUtils.findEntityManagerFactory(rootContext, null);

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Set<Class> administrationConfigs = scanPackageForAdministrationClasses();

        Set<ConfigurationUnits> configurationUnitsCollection = newLinkedHashSet();
        for (Class administrationConfig : administrationConfigs) {
            ConfigurationUnits configurationUnits = unitsFromAutowiredConfiguration(administrationConfig, rootContext.getAutowireCapableBeanFactory());

            configurationUnitsCollection.add(configurationUnits);

            Class repoInterface = createDynamicRepositoryClass(administrationConfig, entityManager);

            BeanDefinition repositoryFactoryBeanDefinition = repositoryFactoryBeanDefinition(repoInterface, entityManager);

            registry.registerBeanDefinition(nameGenerator.generateBeanNameDecapitalized(repoInterface), repositoryFactoryBeanDefinition);
        }

        BeanDefinition configurationBeanDefinition = globalAdministrationConfigurationFactoryBeanDefinition(configurationUnitsCollection);

        registry.registerBeanDefinition(nameGenerator.generateBeanNameDecapitalized(GlobalAdministrationConfiguration.class), configurationBeanDefinition);
    }

    private BeanDefinition globalAdministrationConfigurationFactoryBeanDefinition(Set<ConfigurationUnits> configurationUnits) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(GlobalAdministrationConfigurationFactoryBean.class);
        builder.addPropertyValue("configurationUnits", configurationUnits);
        builder.addPropertyValue("repositories", new RuntimeBeanReference("repositories"));
        return builder.getBeanDefinition();
    }

    private BeanDefinition repositoryFactoryBeanDefinition(Class repoInterface, EntityManager entityManager) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(JpaRepositoryFactoryBean.class);
        builder.addPropertyValue("entityManager", entityManager);
        builder.addPropertyValue("repositoryInterface", repoInterface);
        return builder.getBeanDefinition();
    }

    private Class createDynamicRepositoryClass(Class administrationConfig, EntityManager entityManager) {
        Class domainType = configurationDomainType(administrationConfig);
        EntityType entityType = entityManager.getMetamodel().entity(domainType);
        Class idType = entityType.getIdType().getJavaType();

        return classFactory.createDynamicRepositoryClass(domainType, idType);
    }

    private Set<Class> scanPackageForAdministrationClasses() {
        final ClassScanner classScanner = new AdministrationClassScanner();

        final Set<Class> administrationConfigs = newLinkedHashSet();
        for (String configurationsBasePackage : configurationsBasePackages()) {
            administrationConfigs.addAll(classScanner.scan(configurationsBasePackage));
        }

        return administrationConfigs;
    }

    private String[] configurationsBasePackages() {
        return tokenizeToStringArray(basePackage, CONFIG_LOCATION_DELIMITERS);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }
}