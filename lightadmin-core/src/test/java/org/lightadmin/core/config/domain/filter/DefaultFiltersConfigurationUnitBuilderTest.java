package org.lightadmin.core.config.domain.filter;

import com.google.common.collect.Iterables;
import org.junit.Test;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.lightadmin.api.config.utils.FilterMetadataUtils.filter;

public class DefaultFiltersConfigurationUnitBuilderTest {

    @Test
    public void defaultConfigurationUnitCreatedForDomainType() throws Exception {
        final FiltersConfigurationUnit configurationUnit = defaultFiltersBuilder().build();

        assertEquals(DomainConfigurationUnitType.FILTERS, configurationUnit.getDomainConfigurationUnitType());
        assertEquals(DomainType.class, configurationUnit.getDomainType());
    }

    @Test
    public void simpleFieldFilterDefined() throws Exception {
        final FiltersConfigurationUnit configurationUnit = defaultFiltersBuilder()
                .filter("Name field filter", "name")
                .build();

        assertFiltersDefined(configurationUnit, filter("Name field filter", "name"));
    }

    @Test
    public void fewFieldFiltersDefined() throws Exception {
        final FiltersConfigurationUnit configurationUnit = defaultFiltersBuilder()
                .filter("Name field filter", "name")
                .filter("Surname field filter", "surname")
                .build();

        assertFiltersDefined(configurationUnit,
                filter("Name field filter", "name"),
                filter("Surname field filter", "surname")
        );
    }

    private DefaultFiltersConfigurationUnitBuilder defaultFiltersBuilder() {
        return new DefaultFiltersConfigurationUnitBuilder(DomainType.class);
    }

    private void assertFiltersDefined(final FiltersConfigurationUnit configurationUnit, final FilterMetadata... filters) {
        assertEquals(filters.length, Iterables.size(configurationUnit));

        for (FilterMetadata filter : filters) {
            assertFilterDefined(filter, configurationUnit);
        }
    }

    private void assertFilterDefined(final FilterMetadata filter, final FiltersConfigurationUnit configurationUnit) {
        for (FilterMetadata filterMetadata : configurationUnit) {
            if (equalFilters(filter, filterMetadata)) {
                return;
            }
        }
        fail();
    }

    private boolean equalFilters(final FilterMetadata filter, final FilterMetadata filterMetadata) {
        return filterMetadata.getName().equals(filter.getName()) && filterMetadata.getFieldName().equals(filter.getFieldName());
    }

    private static class DomainType {

    }
}