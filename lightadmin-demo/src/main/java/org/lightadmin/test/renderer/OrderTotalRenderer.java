package org.lightadmin.test.renderer;

import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.test.model.TestOrder;

public class OrderTotalRenderer implements FieldValueRenderer<TestOrder> {

    @Override
    public String apply(final TestOrder order) {
        return String.valueOf(order.getOrderTotal());
    }
}
