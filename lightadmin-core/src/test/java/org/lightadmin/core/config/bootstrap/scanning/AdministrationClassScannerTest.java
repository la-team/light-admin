package org.lightadmin.core.config.bootstrap.scanning;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.config.bootstrap.scanning.config.CorrectAnnotationBasedAdministrationConfig;
import org.lightadmin.core.config.bootstrap.scanning.config.CorrectSuperClassBasedAdministrationConfig;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdministrationClassScannerTest {

    private AdministrationClassScanner testee;

    @Before
    public void setUp() throws Exception {
        testee = new AdministrationClassScanner();
    }

    @Test
    public void configurationClassesFound() throws Exception {
        final Set<Class> configClasses = testee.scan("org.lightadmin.core.config.bootstrap.scanning.config");

        assertEquals(2, configClasses.size());
        assertTrue(configClasses.contains(CorrectAnnotationBasedAdministrationConfig.class));
        assertTrue(configClasses.contains(CorrectSuperClassBasedAdministrationConfig.class));
    }
}