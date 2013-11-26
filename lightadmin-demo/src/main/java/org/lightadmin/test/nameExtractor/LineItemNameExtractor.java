package org.lightadmin.test.nameExtractor;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.test.model.TestLineItem;

public class LineItemNameExtractor implements EntityNameExtractor<TestLineItem> {

    @Override
    public String apply(TestLineItem lineItem) {
        return lineItem.getId() +
                ". Product: " + lineItem.getProduct().getName() +
                "; Amount: " + lineItem.getAmount() +
                "; Total: " + lineItem.getTotal();
    }
}
