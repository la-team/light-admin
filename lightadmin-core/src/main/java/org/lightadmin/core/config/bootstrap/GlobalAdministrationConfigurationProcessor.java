package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.scanning.AdministrationClassScanner;
import org.lightadmin.core.config.bootstrap.scanning.ClassScanner;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.LightAdminConfigurationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.Set;

public class GlobalAdministrationConfigurationProcessor implements BeanPostProcessor {

	private final Environment environment;

	private final DomainTypeAdministrationConfigurationReader<Class> domainTypeAdministrationConfigurationReader;

	public GlobalAdministrationConfigurationProcessor( DomainTypeAdministrationConfigurationReader<Class> domainTypeAdministrationConfigurationReader, Environment environment ) {
		this.environment = environment;

		this.domainTypeAdministrationConfigurationReader = domainTypeAdministrationConfigurationReader;
	}

	@Override
	public Object postProcessAfterInitialization( final Object bean, final String beanName ) throws BeansException {
		if ( !ClassUtils.isAssignableValue( GlobalAdministrationConfiguration.class, bean ) ) {
			return bean;
		}

		final GlobalAdministrationConfiguration globalAdministrationConfiguration = ( GlobalAdministrationConfiguration ) bean;

		final Set<Class> sourceClasses = scanPackageForAdministrationClasses();

		final Set<DomainTypeAdministrationConfiguration> domainTypeAdministrationConfigurations = domainTypeAdministrationConfigurationReader.loadDomainTypeConfiguration( sourceClasses );

		globalAdministrationConfiguration.registerDomainTypeConfigurations( domainTypeAdministrationConfigurations );

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