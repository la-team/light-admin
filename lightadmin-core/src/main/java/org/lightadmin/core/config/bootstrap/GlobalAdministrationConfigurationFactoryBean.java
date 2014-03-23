package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;
import java.util.Set;

public class GlobalAdministrationConfigurationFactoryBean extends AbstractFactoryBean<GlobalAdministrationConfiguration> implements InitializingBean, ApplicationContextAware {

    private EntityManager entityManager;
    private DomainTypeEntityMetadataResolver entityMetadataResolver;
    private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;
    private DomainConfigurationSourceFactory domainConfigurationSourceFactory;
    private ApplicationContext applicationContext;

    private Set<ConfigurationUnits> domainTypeConfigurationUnits;
    private Repositories repositories;

    @Override
    public Class<?> getObjectType() {
        return GlobalAdministrationConfiguration.class;
    }

    @Override
    protected GlobalAdministrationConfiguration createInstance() throws Exception {
        GlobalAdministrationConfiguration globalAdministrationConfiguration = new GlobalAdministrationConfiguration(null);

        for (ConfigurationUnits configurationUnits : domainTypeConfigurationUnits) {
            DomainConfigurationSource configurationSource = domainConfigurationSourceFactory.createConfigurationSource(configurationUnits);

            DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = domainTypeAdministrationConfigurationFactory.createAdministrationConfiguration(configurationSource);

            globalAdministrationConfiguration.registerDomainTypeConfiguration(domainTypeAdministrationConfiguration);

            registerAssociationDomainTypeConfigurations(domainTypeAdministrationConfiguration, globalAdministrationConfiguration);
        }

        return globalAdministrationConfiguration;
    }

    private void registerAssociationDomainTypeConfigurations(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        DomainTypeEntityMetadata<DomainTypeAttributeMetadata> entityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
        for (DomainTypeAttributeMetadata attrMetadata : entityMetadata.getAttributes()) {
            if (!attrMetadata.isAssociation()) {
                continue;
            }

            DomainTypeBasicConfiguration associationTypeConfiguration = this.domainTypeAdministrationConfigurationFactory.createNonManagedDomainTypeConfiguration(attrMetadata.getAssociationDomainType());

            globalAdministrationConfiguration.registerNonDomainTypeConfiguration(associationTypeConfiguration);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.entityMetadataResolver = new JpaDomainTypeEntityMetadataResolver(entityManager);
        this.domainTypeAdministrationConfigurationFactory = new DomainTypeAdministrationConfigurationFactory(repositories, entityManager);

        this.domainConfigurationSourceFactory = new DomainConfigurationSourceFactory(entityMetadataResolver, applicationContext.getAutowireCapableBeanFactory());

        super.afterPropertiesSet();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setRepositories(Repositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setDomainTypeConfigurationUnits(Set<ConfigurationUnits> domainTypeConfigurationUnits) {
        this.domainTypeConfigurationUnits = domainTypeConfigurationUnits;
    }
}