package org.lightadmin.core.config.domain.filter;

import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.lightadmin.core.config.domain.unit.handler.FilterHandler;
import org.lightadmin.core.config.domain.unit.visitor.ConfigurationUnitVisitor;
import org.lightadmin.core.config.domain.unit.visitor.VisitableConfigurationUnit;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class DefaultFiltersConfigurationUnit extends DomainTypeConfigurationUnit implements FiltersConfigurationUnit {

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
    public void doWithFilters(FilterHandler<FilterMetadata> handler) {
        for (FilterMetadata filter : filtersMetadata) {
            handler.doWithFilter(filter);
        }
    }

    @Override
    public void accept(ConfigurationUnitVisitor<VisitableConfigurationUnit> configurationUnitVisitor) {
        configurationUnitVisitor.visit(this);
    }
}