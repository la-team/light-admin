package org.lightadmin.page.fieldDisplay.editView;

import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.TransientFieldOnFormViewConfiguration;
import org.lightadmin.core.reporting.ConfigurationProblemException;

public class TransientField extends SeleniumIntegrationTest {

	//Covers LA-2 comment: https://github.com/max-dev/light-admin/issues/2#issuecomment-12477966
	@Test( expected = ConfigurationProblemException.class )
	public void cannotBeAddedToFormView() {
		registerDomainTypeAdministrationConfiguration( TransientFieldOnFormViewConfiguration.class );
	}
}