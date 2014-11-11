package org.lightadmin.test.renderer;

import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.test.model.TestCustomer;
import org.lightadmin.test.model.TestDiscountProgram;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CustomerDiscountRenderer implements FieldValueRenderer<TestCustomer> {

    //todo: ikostenko: add test for many2many fields on ListView, QuickView, ShowView
    @Override
    public String apply(final TestCustomer customer) {
        List<String> discountPrograms = newArrayList();

        for (TestDiscountProgram discountProgram : customer.getDiscountPrograms()) {
            discountPrograms.add(discountProgram.getName());
        }

        return StringUtils.collectionToDelimitedString(discountPrograms, "<br/>");
    }
}
