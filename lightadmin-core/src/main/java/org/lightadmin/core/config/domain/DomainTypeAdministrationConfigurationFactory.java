package org.lightadmin.core.config.domain;

import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DomainTypeAdministrationConfigurationFactory {

    private final Repositories repositories;
    private final EntityManager entityManager;

    private final MappingContext<? extends PersistentEntity, ? extends PersistentProperty> mappingContext;

    public DomainTypeAdministrationConfigurationFactory(Repositories repositories, EntityManager entityManager) {
        this.repositories = repositories;
        this.entityManager = entityManager;
        this.mappingContext = new JpaMetamodelMappingContext(entityManager.getMetamodel());
    }

    public DomainTypeAdministrationConfiguration createAdministrationConfiguration(ConfigurationUnits configurationUnits) {
        Class<?> domainType = configurationUnits.getDomainType();

        DynamicJpaRepository repository = (DynamicJpaRepository) repositories.getRepositoryFor(domainType);

        PersistentEntity persistentEntity = mappingContext.getPersistentEntity(domainType);

        return new DomainTypeAdministrationConfiguration(repository, persistentEntity, configurationUnits);
    }

    public DomainTypeBasicConfiguration createNonManagedDomainTypeConfiguration(Class<?> domainType) {
        JpaRepository repository = new SimpleJpaRepository(domainType, entityManager);

        PersistentEntity persistentEntity = mappingContext.getPersistentEntity(domainType);

        DefaultEntityMetadataConfigurationUnitBuilder builder = new DefaultEntityMetadataConfigurationUnitBuilder(domainType);

        builder.nameExtractor(EntityNameExtractorFactory.forPersistentEntity(persistentEntity));

        return new NonManagedDomainTypeConfiguration(builder.build(), persistentEntity, repository);
    }
}