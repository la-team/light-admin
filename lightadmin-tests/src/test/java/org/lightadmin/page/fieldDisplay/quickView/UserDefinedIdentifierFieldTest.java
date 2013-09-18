package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.config.OrderTestEntityWithUserDefinedId;
import org.lightadmin.data.Domain;

import static org.lightadmin.util.DomainAsserts.assertQuickViewFields;

@RunWithConfiguration( OrderTestEntityWithUserDefinedId.class )
@LoginOnce( domain = Domain.TEST_ORDERS )
public class UserDefinedIdentifierFieldTest extends SeleniumIntegrationTest {

	@Test
	public void defaultIdentifierFieldIsDisplayed() {
		final QuickViewComponent quickViewComponent = getStartPage().showQuickViewForItem( 1 );

		final String[] actualFieldNames = quickViewComponent.getQuickViewFieldNames();

		assertQuickViewFields( new String[]{ "Order Id:", "Order Total:" }, actualFieldNames );
	}
}
