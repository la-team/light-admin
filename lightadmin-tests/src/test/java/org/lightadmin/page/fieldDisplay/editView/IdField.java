package org.lightadmin.page.fieldDisplay.editView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.OrderTestEntityWithComplexFields;
import org.lightadmin.data.Domain;
import org.lightadmin.page.EditPage;

import static org.junit.Assert.assertTrue;

//Covers LA-74: https://github.com/max-dev/light-admin/issues/74
@RunWithConfiguration( {OrderTestEntityWithComplexFields.class })
@LoginOnce( domain = Domain.TEST_ORDERS )
public class IdField extends SeleniumIntegrationTest {

	private EditPage editPage;

	@Test
	public void isReadonly(){
		editPage = getStartPage().editItem( 2 );

		assertTrue(editPage.isFieldReanOnly("id"));
	}
}
