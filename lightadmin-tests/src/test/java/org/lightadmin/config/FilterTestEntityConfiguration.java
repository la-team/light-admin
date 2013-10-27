package org.lightadmin.config;

import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.test.model.FilterTestEntity;

@SuppressWarnings("unused")
@Administration(FilterTestEntity.class)
public class FilterTestEntityConfiguration {

    public static EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder.pluralName("FilterTest Domain").build();
    }

    public static ScreenContextConfigurationUnit screenContext(final ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("FilterTest Domain Administration").build();
    }

    public static FieldSetConfigurationUnit listView(final FieldSetConfigurationUnitBuilder listViewBuilder) {
        return listViewBuilder.field("textField").caption("Text Field").field("integerField").caption("Integer Field").field("primitiveIntegerField").caption("Primitive Integer Field").field("decimalField").caption("Decimal Field").field("booleanField").caption("Boolean Field").build();
    }

    public static FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder.field("textField").caption("Text Field").field("integerField").caption("Integer Field").field("primitiveIntegerField").caption("Primitive Integer Field").field("decimalField").caption("Decimal Field").field("calculatedField").caption("Calculated Field").field("booleanField").caption("Boolean Field").build();
    }

    public static FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder.filter("Identifier", "id").filter("The Text Field", "textField").filter("The Integer Field", "integerField").filter("The Primitive Integer Field", "primitiveIntegerField").filter("The Decimal Field", "decimalField").filter("The Boolean Field", "booleanField").build();
    }

    public static FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder formViewBuilder) {
        return formViewBuilder.field("textField").caption("Text Field").field("integerField").caption("Integer Field").field("primitiveIntegerField").caption("Primitive Integer Field").field("decimalField").caption("Decimal Field").field("booleanField").caption("Boolean Field").build();
    }
}
