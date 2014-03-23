package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.scanning.AdministrationClassScanner;
import org.lightadmin.core.config.bootstrap.scanning.ClassScanner;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.extension.DynamicRepositoryBeanNameGenerator;
import org.lightadmin.core.extension.DynamicRepositoryClassFactory;
import org.lightadmin.core.extension.JavassistDynamicJpaRepositoryClassFactory;
import org.lightadmin.core.util.DomainConfigurationUtils;
import org.lightadmin.reporting.ProblemReporter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.config.RepositoryBeanNameGenerator;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.servlet.ServletContext;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromAutowiredConfiguration;
import static org.lightadmin.reporting.ProblemReporterFactory.failFastReporter;
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
        AutowireCapableBeanFactory rootFactory = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getAutowireCapableBeanFactory();

        EntityManagerFactory entityManagerFactory = EntityManagerFactoryUtils.findEntityManagerFactory((ListableBeanFactory) rootFactory, null);

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Set<Class> administrationConfigs = scanPackageForAdministrationClasses();

        ProblemReporter problemReporter = failFastReporter();

        ResourceLoader resourceLoader = (ResourceLoader) registry;

        RepositoryBeanNameGenerator generator = new RepositoryBeanNameGenerator();
        generator.setBeanClassLoader(resourceLoader.getClassLoader());


        for (Class administrationConfig : administrationConfigs) {

            Class domainType = DomainConfigurationUtils.configurationDomainType(administrationConfig);
            EntityType entityType = entityManager.getMetamodel().entity(domainType);
            Class idType = entityType.getIdType().getJavaType();

            ConfigurationUnits configurationUnits = unitsFromAutowiredConfiguration(administrationConfig, rootFactory);

            Class repoInterface = classFactory.createDynamicRepositoryClass(domainType, idType);

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(JpaRepositoryFactoryBean.class);
            builder.addPropertyValue("entityManager", entityManager);
            //builder.getRawBeanDefinition().setSource(null);
            builder.addPropertyValue("repositoryInterface", repoInterface);

            registry.registerBeanDefinition(nameGenerator.generateBeanNameDecapitalized(repoInterface), builder.getBeanDefinition());
        }

    }

    /**
     * @see org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension#getEntityManagerBeanDefinitionFor(String, Object)
     */
    private BeanDefinition getEntityManagerBeanDefinitionFor(String entityManagerFactoryBeanName, Object source) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .rootBeanDefinition("org.springframework.orm.jpa.SharedEntityManagerCreator");
        builder.setFactoryMethod("createSharedEntityManager");
        builder.addConstructorArgReference(entityManagerFactoryBeanName);

        AbstractBeanDefinition bean = builder.getRawBeanDefinition();
        bean.setSource(source);

        return bean;
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
