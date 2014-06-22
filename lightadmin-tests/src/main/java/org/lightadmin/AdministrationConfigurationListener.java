package org.lightadmin;

import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
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

        globalConfigurationService.registerDomainTypeConfiguration(configurationUnitsFor(configurationTypes));
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

    private ConfigurationUnits[] configurationUnitsFor(Class<?>... configurationTypes) {
        Collection<ConfigurationUnits> result = newArrayList();
        for (Class<?> configurationType : configurationTypes) {
            result.add(unitsFromConfiguration(configurationType));
        }
        return result.toArray(new ConfigurationUnits[result.size()]);
    }
}