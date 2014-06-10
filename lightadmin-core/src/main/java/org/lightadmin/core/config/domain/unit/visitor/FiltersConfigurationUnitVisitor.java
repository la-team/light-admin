package org.lightadmin.core.config.domain.unit.visitor;

import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.config.domain.unit.handler.FilterHandler;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

public class FiltersConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<FiltersConfigurationUnit> {

    private PersistentEntity persistentEntity;

    public FiltersConfigurationUnitVisitor(PersistentEntity persistentEntity) {
        this.persistentEntity = persistentEntity;
    }

    @Override
    protected void visitInternal(FiltersConfigurationUnit configurationUnit) {
        configurationUnit.doWithFilters(new FilterHandler<FilterMetadata>() {
            @Override
            public void doWithFilter(FilterMetadata filter) {
                PersistentProperty persistentProperty = persistentEntity.getPersistentProperty(filter.getFieldName());
                filter.setPersistentProperty(persistentProperty);
            }
        });
    }
}