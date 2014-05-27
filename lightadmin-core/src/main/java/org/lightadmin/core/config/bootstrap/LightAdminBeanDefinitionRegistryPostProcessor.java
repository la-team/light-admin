package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.parsing.validation.CompositeConfigurationUnitsValidator;
import org.lightadmin.core.config.bootstrap.scanning.AdministrationClassScanner;
import org.lightadmin.core.config.bootstrap.scanning.ClassScanner;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.extension.DynamicRepositoryBeanNameGenerator;
import org.lightadmin.core.persistence.JpaMetamodelMappingContextFactoryBean;
import org.lightadmin.core.persistence.repository.DynamicRepositoryClassFactory;
import org.lightadmin.core.persistence.repository.JavassistDynamicJpaRepositoryClassFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.servlet.ServletContext;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromAutowiredConfiguration;
import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;
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
        this.basePackage = basePackage;
        this.servletContext = servletContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        WebApplicationContext rootContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        EntityManager entityManager = findEntityManager(rootContext);

        ResourceLoader resourceLoader = newResourceLoader(servletContext);

        registry.registerBeanDefinition(JPA_MAPPPING_CONTEXT_BEAN, mappingContext(entityManager));

        registry.registerBeanDefinition(CONFIGURATION_UNITS_VALIDATOR_BEAN, configurationUnitsValidator(resourceLoader));

        Set<Class> administrationConfigs = scanPackageForAdministrationClasses();

        Set<ConfigurationUnits> configurationUnitsCollection = newLinkedHashSet();

        for (Class administrationConfig : administrationConfigs) {
            ConfigurationUnits configurationUnits = unitsFromAutowiredConfiguration(administrationConfig, rootContext.getAutowireCapableBeanFactory());

            configurationUnitsCollection.add(configurationUnits);

            Class repoInterface = createDynamicRepositoryClass(administrationConfig, entityManager);

            registry.registerBeanDefinition(beanName(repoInterface), repositoryFactory(repoInterface, entityManager));
        }

        registry.registerBeanDefinition(beanName(GlobalAdministrationConfiguration.class), globalAdministrationConfigurationFactoryBeanDefinition(configurationUnitsCollection, entityManager));
        registry.registerBeanDefinition(beanName(DomainTypeAdministrationConfigurationFactory.class), domainTypeAdministrationConfigurationFactoryDefinition(entityManager));
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

    private BeanDefinition globalAdministrationConfigurationFactoryBeanDefinition(Set<ConfigurationUnits> configurationUnits, EntityManager entityManager) {
        BeanDefinitionBuilder builder = rootBeanDefinition(GlobalAdministrationConfigurationFactoryBean.class);
        builder.addPropertyReference("domainTypeAdministrationConfigurationFactory", beanName(DomainTypeAdministrationConfigurationFactory.class));
        builder.addPropertyValue("domainTypeConfigurationUnits", configurationUnits);
        builder.addPropertyValue("entityManager", entityManager);
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

        return entityManagerFactory.createEntityManager();
    }

    private ServletContextResourceLoader newResourceLoader(ServletContext servletContext) {
        return new ServletContextResourceLoader(servletContext);
    }

    private Class createDynamicRepositoryClass(Class administrationConfig, EntityManager entityManager) {
        Class domainType = configurationDomainType(administrationConfig);
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