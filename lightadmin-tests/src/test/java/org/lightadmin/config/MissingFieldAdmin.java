package org.lightadmin.config;

import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.builder.FiltersConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.test.model.TestOrder;

@SuppressWarnings("unused")
@Administration(TestOrder.class)
public class MissingFieldAdmin {

    public static FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Missing Field", "missingField").build();
    }
}
