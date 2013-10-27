package org.lightadmin.core.config.domain.filter;

import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataAware;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class DefaultFiltersConfigurationUnit extends DomainTypeConfigurationUnit implements FiltersConfigurationUnit, DomainTypeEntityMetadataAware {

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
    public void setDomainTypeEntityMetadata(final DomainTypeEntityMetadata domainTypeEntityMetadata) {
        for (FilterMetadata filterMetadata : filtersMetadata) {
            filterMetadata.setAttributeMetadata(domainTypeEntityMetadata.getAttribute(filterMetadata.getFieldName()));
        }
    }
}