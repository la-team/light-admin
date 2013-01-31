package org.lightadmin;

import org.junit.runner.RunWith;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.util.DomainConfigurationUtils;
import org.lightadmin.util.ExtendedWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.URL;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( loader = AnnotationConfigContextLoader.class, classes = SeleniumConfig.class )
public abstract class SeleniumIntegrationTest {

	@Autowired
	private SeleniumContext seleniumContext;

	@Autowired
	private GlobalConfigurationManagementService globalConfigurationManagementService;

	protected void registerDomainTypeAdministrationConfiguration( Class configurationClass ) {
		globalConfigurationManagementService.registerDomainTypeConfiguration( ConfigurationUnitsConverter.unitsFromConfiguration( configurationClass ) );
	}

	protected void removeDomainTypeAdministrationConfiguration( Class configurationClass ) {
		globalConfigurationManagementService.removeDomainTypeAdministrationConfiguration( DomainConfigurationUtils.configurationDomainType( configurationClass ) );
	}

	protected void removeAllDomainTypeAdministrationConfigurations() {
		globalConfigurationManagementService.removeAllDomainTypeAdministrationConfigurations();
	}

	protected ExtendedWebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}

	protected URL baseUrl() {
		return seleniumContext.getBaseUrl();
	}

	protected long webDriverTimeout(){
		return seleniumContext.getWebDriverWaitTimeout();
	}
}