package org.lightadmin.core.config.domain;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DomainTypeAdministrationConfigurationFactory {

    private final Repositories repositories;
    private final DomainTypeEntityMetadataResolver entityMetadataResolver;
    private final EntityManager entityManager;

    public DomainTypeAdministrationConfigurationFactory(Repositories repositories, EntityManager entityManager) {
        this.repositories = repositories;
        this.entityMetadataResolver = new JpaDomainTypeEntityMetadataResolver(entityManager);
        this.entityManager = entityManager;
    }

    public DomainTypeAdministrationConfiguration createAdministrationConfiguration(DomainConfigurationSource domainConfigurationSource) {
        DynamicJpaRepository repository = (DynamicJpaRepository) repositories.getRepositoryFor(domainConfigurationSource.getDomainType());

        return new DomainTypeAdministrationConfiguration(domainConfigurationSource, repository);
    }

    public DomainTypeBasicConfiguration createNonManagedDomainTypeConfiguration(Class<?> domainType) {
        JpaRepository repository = new SimpleJpaRepository(domainType, entityManager);

        DomainTypeEntityMetadata entityMetadata = entityMetadataResolver.resolveEntityMetadata(domainType);

        DefaultEntityMetadataConfigurationUnitBuilder builder = new DefaultEntityMetadataConfigurationUnitBuilder(domainType);
        builder.nameExtractor(EntityNameExtractorFactory.forPersistentEntity(entityMetadata));

        return new NonManagedDomainTypeConfiguration(builder.build(), entityMetadata, repository);
    }
}