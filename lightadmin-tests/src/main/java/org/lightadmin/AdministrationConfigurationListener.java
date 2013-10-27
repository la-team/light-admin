package org.lightadmin;

import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromConfiguration;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.ObjectUtils.isEmpty;

public class AdministrationConfigurationListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(final TestContext testContext) throws Exception {
        final Class<?>[] configurationTypes = configurationType(testContext.getTestClass());
        if (isEmpty(configurationTypes)) {
            return;
        }

        final GlobalConfigurationManagementService globalConfigurationService = globalConfigurationService(testContext);

        globalConfigurationService.removeAllDomainTypeAdministrationConfigurations();

        for (Class<?> configurationType : configurationTypes) {
            globalConfigurationService.registerDomainTypeConfiguration(unitsFromConfiguration(configurationType));
        }
    }

    @Override
    public void afterTestClass(final TestContext testContext) throws Exception {
        if (!isEmpty(configurationType(testContext.getTestClass()))) {
            globalConfigurationService(testContext).removeAllDomainTypeAdministrationConfigurations();
        }
    }

    public static Class<?>[] configurationType(Class clazz) {
        final RunWithConfiguration annotation = findAnnotation(clazz, RunWithConfiguration.class);

        return annotation == null ? null : annotation.value();
    }

    private GlobalConfigurationManagementService globalConfigurationService(final TestContext testContext) {
        return testContext.getApplicationContext().getBean(GlobalConfigurationManagementService.class);
    }
}