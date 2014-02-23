package org.lightadmin.core.config.domain;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.data.repository.support.Repositories;

public class DomainTypeAdministrationConfigurationFactory {

    private final Repositories repositories;
    private final DomainTypeEntityMetadataResolver entityMetadataResolver;

    public DomainTypeAdministrationConfigurationFactory(Repositories repositories, DomainTypeEntityMetadataResolver entityMetadataResolver) {
        this.repositories = repositories;
        this.entityMetadataResolver = entityMetadataResolver;
    }

    @SuppressWarnings("unchecked")
    public DomainTypeAdministrationConfiguration createAdministrationConfiguration(DomainConfigurationSource domainConfigurationSource) {
//        final DynamicJpaRepository<?, ? extends Serializable> repository = dynamicJpaRepositoryFactory.createRepository(domainConfigurationSource.getDomainType());

        return new DomainTypeAdministrationConfiguration(domainConfigurationSource, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public DomainTypeBasicConfiguration createNonManagedDomainTypeConfiguration(Class<?> domainType) {
//        DynamicJpaRepository<?, ? extends Serializable> repository = dynamicJpaRepositoryFactory.createRepository(domainType);
        DomainTypeEntityMetadata entityMetadata = entityMetadataResolver.resolveEntityMetadata(domainType);

        DefaultEntityMetadataConfigurationUnitBuilder builder = new DefaultEntityMetadataConfigurationUnitBuilder(domainType);
        builder.nameExtractor(EntityNameExtractorFactory.forPersistentEntity(entityMetadata));

        return new NonManagedDomainTypeConfiguration(builder.build(), entityMetadata, null);
    }
}