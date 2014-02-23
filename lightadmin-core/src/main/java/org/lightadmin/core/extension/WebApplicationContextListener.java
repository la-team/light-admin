package org.lightadmin.core.extension;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidator;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidatorFactory;
import org.lightadmin.core.config.bootstrap.scanning.AdministrationClassScanner;
import org.lightadmin.core.config.bootstrap.scanning.ClassScanner;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.reporting.ProblemReporter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.reporting.ProblemReporterFactory.failFastReporter;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

public class WebApplicationContextListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationEventPublisherAware {

    private static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    private final DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;
    private final GlobalAdministrationConfiguration globalAdministrationConfiguration;
    private final DomainConfigurationSourceFactory domainConfigurationSourceFactory;

    private final DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory;
    private final LightAdminConfiguration lightAdminConfiguration;
    private ApplicationEventPublisher applicationEventPublisher;

    public WebApplicationContextListener(final GlobalAdministrationConfiguration globalAdministrationConfiguration,
                                         final DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory,
                                         final DomainConfigurationSourceFactory domainConfigurationSourceFactory,
                                         final DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory,
                                         final LightAdminConfiguration lightAdminConfiguration) {

        this.globalAdministrationConfiguration = globalAdministrationConfiguration;
        this.domainConfigurationSourceFactory = domainConfigurationSourceFactory;
        this.domainTypeAdministrationConfigurationFactory = domainTypeAdministrationConfigurationFactory;
        this.configurationSourceValidatorFactory = configurationSourceValidatorFactory;
        this.lightAdminConfiguration = lightAdminConfiguration;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final Set<Class> metadataClasses = scanPackageForAdministrationClasses();

        final DomainConfigurationSourceValidator validator = configurationSourceValidatorFactory.getValidator();

        final ProblemReporter problemReporter = failFastReporter();

        for (Class metadataClass : metadataClasses) {
            final DomainConfigurationSource configurationSource = domainConfigurationSourceFactory.createConfigurationSource(metadataClass);

//            validator.validate(configurationSource, problemReporter);

            globalAdministrationConfiguration.registerDomainTypeConfiguration(
                    domainTypeAdministrationConfigurationFactory.createAdministrationConfiguration(configurationSource)
            );
        }

        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(new AdministrationConfigurationRefreshedEvent(globalAdministrationConfiguration));
        }
    }

    private Set<Class> scanPackageForAdministrationClasses() {
        final ClassScanner classScanner = new AdministrationClassScanner();

        final Set<Class> administrationConfigs = newLinkedHashSet();
        for (String configurationsBasePackage : configurationsBasePackages()) {
            administrationConfigs.addAll(classScanner.scan(configurationsBasePackage));
        }

        return administrationConfigs;
    }


    private String[] configurationsBasePackages() {
        return tokenizeToStringArray(lightAdminConfiguration.getBasePackage(), CONFIG_LOCATION_DELIMITERS);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}