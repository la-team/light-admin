package org.lightadmin.core.config.management.rmi;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.context.LightAdminRepositoryRestMvcConfiguration;
import org.lightadmin.core.test.IntegrationTest;
import org.lightadmin.core.test.LightAdminConfigurationContextLoader;
import org.lightadmin.core.test.LightAdminTestConfiguration;
import org.lightadmin.core.test.model.Address;
import org.lightadmin.core.test.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromConfiguration;

@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = LightAdminConfigurationContextLoader.class,
        classes = {LightAdminTestConfiguration.class, LightAdminRepositoryRestMvcConfiguration.class})
public class GlobalConfigurationManagementServiceImplTest {

    @Autowired
    private GlobalConfigurationManagementService globalConfigurationManagementService;

    @Before
    public void setUp() throws Exception {
        globalConfigurationManagementService.removeAllDomainTypeAdministrationConfigurations();
    }

    @Test
    public void runtimeConfigurationRegistration() {
        globalConfigurationManagementService.registerDomainTypeConfiguration(unitsFromConfiguration(AddressConfiguration.class));

        assertNotNull(globalConfigurationManagementService.getRegisteredDomainTypeConfiguration(Address.class));
    }

    @Test
    public void runtimeConfigurationRemoval() {
        globalConfigurationManagementService.registerDomainTypeConfiguration(unitsFromConfiguration(AddressConfiguration.class));

        globalConfigurationManagementService.removeDomainTypeAdministrationConfiguration(Address.class);

        assertNull(globalConfigurationManagementService.getRegisteredDomainTypeConfiguration(Address.class));
    }

    @Test
    public void runtimeMultipleConfigurationsRegistration() {
        assertTrue(globalConfigurationManagementService.getRegisteredDomainTypeConfigurations().isEmpty());

        globalConfigurationManagementService.registerDomainTypeConfiguration(unitsFromConfiguration(AddressConfiguration.class), unitsFromConfiguration(CustomerConfiguration.class));

        assertEquals(2, globalConfigurationManagementService.getRegisteredDomainTypeConfigurations().size());

        assertNotNull(globalConfigurationManagementService.getRegisteredDomainTypeConfiguration(Address.class));
        assertNotNull(globalConfigurationManagementService.getRegisteredDomainTypeConfiguration(Customer.class));
    }

    @Test
    public void runtimeConfigurationsCleanup() {
        globalConfigurationManagementService.registerDomainTypeConfiguration(unitsFromConfiguration(AddressConfiguration.class), unitsFromConfiguration(CustomerConfiguration.class));

        globalConfigurationManagementService.removeAllDomainTypeAdministrationConfigurations();

        assertTrue(globalConfigurationManagementService.getRegisteredDomainTypeConfigurations().isEmpty());
    }

    @Administration(Address.class)
    private static class AddressConfiguration {

    }

    @Administration(Customer.class)
    private static class CustomerConfiguration {

    }
}