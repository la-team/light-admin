package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.config.OrderTestEntityWithoutQuickViewFields;
import org.lightadmin.data.Domain;

import static org.lightadmin.util.DomainAsserts.assertFieldValues;
import static org.lightadmin.util.DomainAsserts.assertQuickViewFields;

@RunWithConfiguration(OrderTestEntityWithoutQuickViewFields.class)
@LoginOnce(domain = Domain.TEST_ORDERS)
public class DefaultFieldsTest extends SeleniumIntegrationTest {

    @Test
    public void allPersistentFieldsAreDisplayedWhenQuickViewIsNotConfigured() {
        final QuickViewComponent quickViewComponent = getStartPage().showQuickViewForItem(3);

        String[] quickViewFieldNames = quickViewComponent.getQuickViewFieldNames();
        String[] quickViewFieldValues = quickViewComponent.getQuickViewFieldValues();

        assertQuickViewFields(new String[]{
                "Id:",
                "Customer:",
                "DueDate:",
                "LineItems:",
                "Name:",
                "ShippingAddresses:"
        }, quickViewFieldNames);

        assertFieldValues(new String[]{
                "3",
                "TestCustomer #17",
                "2010-01-01",
                "TestLineItem #101\n" + "TestLineItem #102\n" + "TestLineItem #103",
                "Order3: 3 line items",
                " "
        }, quickViewFieldValues);
    }
}
