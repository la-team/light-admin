package org.lightadmin.page.fieldDisplay;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.InvalidFieldAdmin;

import static org.hamcrest.CoreMatchers.containsString;


public class InvalidFieldTest extends SeleniumIntegrationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //Covers LA-72 (https://github.com/max-dev/light-admin/issues/72)
    @Test
    public void correctErrorMessageIsShown() {

        CombinableMatcher<String> combinableMatcher = CoreMatchers.<String>both(containsString("Domain Configuration")).and(containsString("InvalidFieldAdmin"))
                .and(containsString("Unit")).and(containsString("filters"))
                .and(containsString("Missing property 'Missing Field' defined!"));

        expectedException.expectMessage(
                combinableMatcher);

        registerDomainTypeAdministrationConfiguration(InvalidFieldAdmin.class);
    }
}
