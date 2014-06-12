package org.lightadmin.core.config.management.jmx;

import com.google.common.collect.Sets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.context.LightAdminRepositoryRestMvcConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter;
import org.lightadmin.core.test.IntegrationTest;
import org.lightadmin.core.test.LightAdminConfigurationContextLoader;
import org.lightadmin.core.test.LightAdminTestConfiguration;
import org.lightadmin.core.test.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertTrue;

@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = LightAdminConfigurationContextLoader.class,
        classes = {LightAdminTestConfiguration.class, LightAdminRepositoryRestMvcConfiguration.class})
public class LightAdminConfigurationMonitoringServiceMBeanTest {

    @Autowired
    private GlobalConfigurationManagementService globalConfigurationManagementService;

    private LightAdminConfigurationMonitoringServiceMBean testee;

    @Before
    public void setUp() throws Exception {
        testee = new LightAdminConfigurationMonitoringServiceMBean(globalConfigurationManagementService);

        globalConfigurationManagementService.registerDomainTypeConfiguration(ConfigurationUnitsConverter.unitsFromConfiguration(AddressConfiguration.class));
    }

    @After
    public void tearDown() throws Exception {
        globalConfigurationManagementService.removeDomainTypeAdministrationConfiguration(AddressConfiguration.class);
    }

    @Test
    public void domainTypesInfoRetrieval() throws Exception {
        final Set<String> actualDomainTypes = testee.getDomainTypes();

        final Set<String> expectedDomainTypeNames = newHashSet("address");

        assertTrue(Sets.difference(expectedDomainTypeNames, actualDomainTypes).isEmpty());
    }

    @Test
    public void domainConfigurationNamesRetrieval() throws Exception {
        final Set<String> actualConfigurationNames = testee.getDomainTypeAdministrationConfigurations();

        final Set<String> expectedConfigurationNames = newHashSet("AddressConfiguration");

        assertTrue(Sets.difference(expectedConfigurationNames, actualConfigurationNames).isEmpty());
    }

    @Administration(Address.class)
    private static class AddressConfiguration {

    }
}