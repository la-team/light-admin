package org.lightadmin.page.fieldDisplay.listView;

import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.ChildEntityConfiguration;
import org.lightadmin.core.reporting.ConfigurationProblemException;

public class ReferenceFieldTest extends SeleniumIntegrationTest {

    //Covers LA-5: https://github.com/max-dev/light-admin/issues/5#issuecomment-10261092
    @Test(expected = ConfigurationProblemException.class)
    public void referenceFieldNotAddedToEntityIsNotDisplayed() {
        registerDomainTypeAdministrationConfiguration(ChildEntityConfiguration.class);
    }
}
