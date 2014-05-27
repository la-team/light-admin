package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.parsing.validation.ConfigurationUnitsValidator;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.reporting.ProblemReporter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;
import java.util.Set;

import static org.lightadmin.reporting.ProblemReporterFactory.failFastReporter;

public class GlobalAdministrationConfigurationFactoryBean extends AbstractFactoryBean<GlobalAdministrationConfiguration> implements InitializingBean {

    private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;

    private Set<ConfigurationUnits> domainTypeConfigurationUnits;

    private EntityManager entityManager;

    private Repositories repositories;

    private ConfigurationUnitsValidator<ConfigurationUnits> configurationUnitsValidator;

    @Override
    public Class<?> getObjectType() {
        return GlobalAdministrationConfiguration.class;
    }

    @Override
    protected GlobalAdministrationConfiguration createInstance() throws Exception {
        GlobalAdministrationConfiguration globalAdministrationConfiguration = new GlobalAdministrationConfiguration();

        final ProblemReporter problemReporter = failFastReporter();

        for (ConfigurationUnits configurationUnits : domainTypeConfigurationUnits) {

            configurationUnitsValidator.validate(configurationUnits, problemReporter);

            DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = domainTypeAdministrationConfigurationFactory.createAdministrationConfiguration(configurationUnits);

            globalAdministrationConfiguration.registerDomainTypeConfiguration(domainTypeAdministrationConfiguration);

            registerAssociationDomainTypeConfigurations(domainTypeAdministrationConfiguration, globalAdministrationConfiguration);
        }

        return globalAdministrationConfiguration;
    }

    private void registerAssociationDomainTypeConfigurations(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, final GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        PersistentEntity persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();

        persistentEntity.doWithAssociations(new SimpleAssociationHandler() {
            @Override
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                Class<?> associationDomainType = association.getInverse().getActualType();

                if (isManagedEntity(associationDomainType) && !repositories.hasRepositoryFor(associationDomainType)) {
                    DomainTypeBasicConfiguration associationTypeConfiguration = domainTypeAdministrationConfigurationFactory.createNonManagedDomainTypeConfiguration(associationDomainType);
                    globalAdministrationConfiguration.registerNonDomainTypeConfiguration(associationTypeConfiguration);
                }
            }
        });
    }

    private boolean isManagedEntity(Class type) {
        try {
            return entityManager.getMetamodel().entity(type) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void setDomainTypeConfigurationUnits(Set<ConfigurationUnits> domainTypeConfigurationUnits) {
        this.domainTypeConfigurationUnits = domainTypeConfigurationUnits;
    }

    public void setDomainTypeAdministrationConfigurationFactory(DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory) {
        this.domainTypeAdministrationConfigurationFactory = domainTypeAdministrationConfigurationFactory;
    }

    public void setConfigurationUnitsValidator(ConfigurationUnitsValidator configurationUnitsValidator) {
        this.configurationUnitsValidator = configurationUnitsValidator;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setRepositories(Repositories repositories) {
        this.repositories = repositories;
    }
}