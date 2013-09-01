package org.lightadmin.page;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;

import static org.junit.Assert.assertTrue;

@RunWithConfiguration( FilterTestEntityConfiguration.class )
@LoginOnce( domain = Domain.FILTER_TEST_DOMAIN)
public class BreadcrumbsTest extends SeleniumIntegrationTest {

	@Test
	public void dashboardBreadcrumbPresentOnListView() throws Exception {
		assertTrue( getStartPage().dashboardBreadcrumbItemLinkPresent() );
	}

	//TODO: iko: to add more tests
}
