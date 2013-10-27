package org.lightadmin.config;

import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.test.model.TestOrder;
import org.lightadmin.test.renderer.LineItemRenderer;
import org.lightadmin.test.renderer.OrderTotalRenderer;

@SuppressWarnings("unused")
@Administration(TestOrder.class)
public class OrderTestEntityWithComplexFields {

    public static EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder.pluralName("Test Order Domain").build();
    }

    public static ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("Administration of Test Order Domain").build();
    }

    public static FieldSetConfigurationUnit listView(FieldSetConfigurationUnitBuilder listViewBuilder) {
        return listViewBuilder.field("customer").caption("Customer").field("shippingAddresses").caption("Shipping Addresses").renderable(new LineItemRenderer()).caption("Line Items").renderable(new OrderTotalRenderer()).caption("Order Total").field("dueDate").caption("Order Due Date").build();
    }

    public static FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder.field("customer").caption("Customer").field("shippingAddresses").caption("Shipping Addresses").renderable(new LineItemRenderer()).caption("Line Items").renderable(new OrderTotalRenderer()).caption("Order Total").field("dueDate").caption("Order Due Date").build();
    }

    public static FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder.filter("Id", "id").filter("Customer", "customer").filter("Shipping Addresses", "shippingAddresses").filter("Line Items", "lineItems").filter("Order Due Date", "dueDate").build();
    }

    public static FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder formViewBuilder) {
        return formViewBuilder.field("customer").caption("Customer").field("shippingAddresses").caption("Shipping Addresses").field("lineItems").caption("Line Items").field("dueDate").caption("Order Due Date").build();
    }
}
