package org.lightadmin;

import org.junit.runner.RunWith;
import org.lightadmin.api.config.management.rmi.DataManipulationService;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.testContext.TestProfileSource;
import org.lightadmin.util.ExtendedWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.net.URL;

import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        AdministrationConfigurationListener.class, LoginListener.class
})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = SeleniumConfig.class)
@ProfileValueSourceConfiguration(TestProfileSource.class)
public abstract class SeleniumIntegrationTest {

    @Autowired
    private SeleniumContext seleniumContext;

    @Autowired
    private GlobalConfigurationManagementService globalConfigurationManagementService;

    @Autowired
    private DataManipulationService dataManipulationService;

    @Autowired
    private LoginService loginService;

    protected void registerDomainTypeAdministrationConfiguration(Class configurationClass) {
        globalConfigurationManagementService.registerDomainTypeConfiguration(unitsFromConfiguration(configurationClass));
    }

    protected void removeAllDomainTypeAdministrationConfigurations() {
        globalConfigurationManagementService.removeAllDomainTypeAdministrationConfigurations();
    }

    protected void repopulateDatabase() {
        truncateDatabase();
        populateDatabase();
    }

    protected void truncateDatabase() {
        dataManipulationService.truncateDatabase();
    }

    protected void populateDatabase() {
        dataManipulationService.populateDatabase();
    }

    protected ExtendedWebDriver webDriver() {
        return seleniumContext.getWebDriver();
    }

    protected URL baseUrl() {
        return seleniumContext.getBaseUrl();
    }

    protected long webDriverTimeout() {
        return seleniumContext.getWebDriverWaitTimeout();
    }

    protected ListViewPage getStartPage() {
        return loginService.getStartPage();
    }
}