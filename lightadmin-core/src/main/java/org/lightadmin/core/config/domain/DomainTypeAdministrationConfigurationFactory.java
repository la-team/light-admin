package org.lightadmin.core.config.domain;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.DynamicJpaRepositoryFactory;

import java.io.Serializable;

public class DomainTypeAdministrationConfigurationFactory {

    private final DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory;
    private final DomainTypeEntityMetadataResolver entityMetadataResolver;

    public DomainTypeAdministrationConfigurationFactory(DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory, DomainTypeEntityMetadataResolver entityMetadataResolver) {
        this.entityMetadataResolver = entityMetadataResolver;
        this.dynamicJpaRepositoryFactory = dynamicJpaRepositoryFactory;
    }

    @SuppressWarnings("unchecked")
    public DomainTypeAdministrationConfiguration createAdministrationConfiguration(DomainConfigurationSource domainConfigurationSource) {
        final DynamicJpaRepository<?, ? extends Serializable> repository = dynamicJpaRepositoryFactory.createRepository(domainConfigurationSource.getDomainType());

        return new DomainTypeAdministrationConfiguration(domainConfigurationSource, repository);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public DomainTypeBasicConfiguration createNonManagedDomainTypeConfiguration(Class<?> domainType) {
        DynamicJpaRepository<?, ? extends Serializable> repository = dynamicJpaRepositoryFactory.createRepository(domainType);
        DomainTypeEntityMetadata entityMetadata = entityMetadataResolver.resolveEntityMetadata(domainType);

        DefaultEntityMetadataConfigurationUnitBuilder builder = new DefaultEntityMetadataConfigurationUnitBuilder(domainType);
        builder.nameExtractor(EntityNameExtractorFactory.forPersistentEntity(entityMetadata));

        return new NonManagedDomainTypeConfiguration(builder.build(), entityMetadata, repository);
    }
}