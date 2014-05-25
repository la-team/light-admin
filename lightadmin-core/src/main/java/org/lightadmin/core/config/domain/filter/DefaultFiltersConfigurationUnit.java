package org.lightadmin.core.config.domain.filter;

import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.PersistentEntityAware;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class DefaultFiltersConfigurationUnit extends DomainTypeConfigurationUnit implements FiltersConfigurationUnit, PersistentEntityAware {

    private final Set<FilterMetadata> filtersMetadata;

    DefaultFiltersConfigurationUnit(Class<?> domainType, final Set<FilterMetadata> filtersMetadata) {
        super(domainType);

        Assert.notNull(filtersMetadata);

        this.filtersMetadata = newLinkedHashSet(filtersMetadata);
    }

    @Override
    public Iterator<FilterMetadata> iterator() {
        return filtersMetadata.iterator();
    }

    public int size() {
        return filtersMetadata.size();
    }

    @Override
    public DomainConfigurationUnitType getDomainConfigurationUnitType() {
        return DomainConfigurationUnitType.FILTERS;
    }

    @Override
    public void setPersistentEntity(final PersistentEntity persistenEntity) {
        for (FilterMetadata filterMetadata : filtersMetadata) {
            PersistentProperty persistentProperty = persistenEntity.getPersistentProperty(filterMetadata.getFieldName());

            filterMetadata.setPersistentProperty(persistentProperty);
        }
    }
}