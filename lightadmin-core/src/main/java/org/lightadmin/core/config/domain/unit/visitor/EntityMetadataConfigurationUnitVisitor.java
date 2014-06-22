package org.lightadmin.core.config.domain.unit.visitor;

import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnit;
import org.springframework.data.mapping.PersistentEntity;

import static org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory.forPersistentEntity;

public class EntityMetadataConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<DefaultEntityMetadataConfigurationUnit> {

    private PersistentEntity persistenEntity;

    public EntityMetadataConfigurationUnitVisitor(PersistentEntity persistenEntity) {
        this.persistenEntity = persistenEntity;
    }

    @Override
    protected void visitInternal(DefaultEntityMetadataConfigurationUnit configurationUnit) {
        if (configurationUnit.getNameExtractor() == null) {
            configurationUnit.setNameExtractor(forPersistentEntity(configurationUnit.getSingularName(), this.persistenEntity));
        }
    }
}