package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidator;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidatorFactory;
import org.lightadmin.core.config.bootstrap.scanning.AdministrationClassScanner;
import org.lightadmin.core.config.bootstrap.scanning.ClassScanner;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.reporting.ProblemReporterFactory;
import org.lightadmin.core.util.LightAdminConfigurationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.Set;

public class GlobalAdministrationConfigurationProcessor implements BeanPostProcessor {

	private final DomainTypeAdministrationConfigFactory domainTypeAdministrationConfigFactory;
	private final DomainConfigurationSourceFactory domainConfigurationSourceFactory;

	private final DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory;

	private final Environment environment;

	public GlobalAdministrationConfigurationProcessor( final DomainTypeAdministrationConfigFactory domainTypeAdministrationConfigFactory,
													   final DomainConfigurationSourceFactory domainConfigurationSourceFactory,
													   final DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory,
													   final Environment environment ) {

		this.domainConfigurationSourceFactory = domainConfigurationSourceFactory;
		this.domainTypeAdministrationConfigFactory = domainTypeAdministrationConfigFactory;
		this.configurationSourceValidatorFactory = configurationSourceValidatorFactory;

		this.environment = environment;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public Object postProcessAfterInitialization( final Object bean, final String beanName ) throws BeansException {
		if ( !ClassUtils.isAssignableValue( GlobalAdministrationConfiguration.class, bean ) ) {
			return bean;
		}

		final GlobalAdministrationConfiguration globalAdministrationConfiguration = ( GlobalAdministrationConfiguration ) bean;

		final Set<Class> metadataClasses = scanPackageForAdministrationClasses();

		final DomainConfigurationSourceValidator validator = configurationSourceValidatorFactory.getValidator();

		final ProblemReporter problemReporter = ProblemReporterFactory.failFastReporter();

		for ( Class metadataClass : metadataClasses ) {
			final DomainConfigurationSource configurationSource = domainConfigurationSourceFactory.createConfigurationSource( metadataClass );

			validator.validate( configurationSource, problemReporter );

			globalAdministrationConfiguration.registerDomainTypeConfiguration( domainTypeAdministrationConfigFactory.createAdministrationConfiguration( configurationSource ) );
		}

		return globalAdministrationConfiguration;
	}

	private Set<Class> scanPackageForAdministrationClasses() {
		final ClassScanner classScanner = new AdministrationClassScanner();

		return classScanner.scan( configurationsBasePackage() );
	}

	private String configurationsBasePackage() {
		return environment.getProperty( LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_PACKAGE );
	}

	@Override
	public Object postProcessBeforeInitialization( final Object bean, final String beanName ) throws BeansException {
		return bean;
	}
}