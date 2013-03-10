package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.config.OrderTestEntityWithDefaultId;
import org.lightadmin.data.Domain;

import static org.junit.Assert.assertTrue;
import static org.lightadmin.util.DomainAsserts.assertFieldValues;

@RunWithConfiguration( {OrderTestEntityWithDefaultId.class})
@LoginOnce( domain = Domain.TEST_ORDERS )
public class DisplayTest extends SeleniumIntegrationTest {

	@Before
	public void refreshListView() {
		getStartPage().navigateToDomain( Domain.TEST_ORDERS );
	}

	@Test
	public void canBeHidden() {
		final QuickViewComponent quickViewComponent = getStartPage().showQuickViewForItem( 1 );

		quickViewComponent.hide();

		assertTrue( quickViewComponent.isHidden() );
	}

	@Test
	public void correctInfoIsDisplayedAfterSorting() {
		getStartPage().getDataTable().getColumn( "Name" ).sortDescending();

		final QuickViewComponent quickViewComponent = getStartPage().showQuickViewForItem( 1 );
		final String[] actualFieldValues = quickViewComponent.getQuickViewFieldValues();

		assertFieldValues( new String[]{ "1", "62100.00" }, actualFieldValues );
	}

	@Test
	public void infoCanBeDisplayedForMultipleItems() {
		final QuickViewComponent quickViewComponent1 = getStartPage().showQuickViewForItem( 1 );
		final String[] actualFieldValues1 = quickViewComponent1.getQuickViewFieldValues();

		final QuickViewComponent quickViewComponent2 = getStartPage().showQuickViewForItem( 3 );
		final String[] actualFieldValues2 = quickViewComponent2.getQuickViewFieldValues();

		assertFieldValues( new String[]{ "1", "62100.00" }, actualFieldValues1 );
		assertFieldValues( new String[]{ "3", "226308.00" }, actualFieldValues2 );
	}

	//TODO: iko: add test covering dynamic fields
}