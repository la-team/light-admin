package org.lightadmin.config;

import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.builder.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.api.config.builder.FiltersConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.test.model.FilterTestEntity;

import static org.lightadmin.api.config.utils.FilterMetadataUtils.filter;

@Administration(FilterTestEntity.class)
public class AlternativeFilterConfiguration {

    public static EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder.pluralName("FilterTest Domain").build();
    }

    public static FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder.filters(
                filter().field("id").caption("Identifier").build(),
                filter().field("textField").caption("The Text Field").build(),
                filter().field("integerField").caption("The Integer Field").build(),
                filter().field("primitiveIntegerField").caption("The Primitive Integer Field").build(),
                filter().field("decimalField").caption("The Decimal Field").build(),
                filter().field("booleanField").caption("The Boolean Field").build()
        ).build();
    }
}
