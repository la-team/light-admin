package org.lightadmin.core.config.management.rmi;

import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidator;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidatorFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.reporting.ProblemReporterFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.rest.webmvc.RepositoryAwareMappingHttpMessageConverter;
import org.springframework.data.rest.webmvc.json.RepositoryAwareJacksonModule;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GlobalConfigurationManagementRMIService implements GlobalConfigurationManagementService, BeanFactoryAware {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Autowired
	private DomainConfigurationSourceFactory domainConfigurationSourceFactory;

	@Autowired
	private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;

	@Autowired
	private DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory;

	@Autowired
	private RepositoryAwareMappingHttpMessageConverter repositoryAwareMappingHttpMessageConverter;

	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	@Override
	@SuppressWarnings( "unchecked" )
	public void registerDomainTypeConfiguration( final ConfigurationUnits configurationUnits ) {
		final DomainConfigurationSource configurationSource = domainConfigurationSourceFactory.createConfigurationSource( configurationUnits );

		final DomainConfigurationSourceValidator configurationSourceValidator = configurationSourceValidatorFactory.getValidator();

		configurationSourceValidator.validate( configurationSource, ProblemReporterFactory.failFastReporter() );

		final DomainTypeAdministrationConfiguration administrationConfiguration = domainTypeAdministrationConfigurationFactory.createAdministrationConfiguration( configurationSource );

		globalAdministrationConfiguration.registerDomainTypeConfiguration( administrationConfiguration );

		final Field jacksonModuleField = ReflectionUtils.findField( RepositoryAwareMappingHttpMessageConverter.class, "jacksonModule" );

		ReflectionUtils.makeAccessible( jacksonModuleField );

		final RepositoryAwareJacksonModule repositoryAwareJacksonModule = repositoryAwareJacksonModule();

		ReflectionUtils.setField( jacksonModuleField, repositoryAwareMappingHttpMessageConverter, repositoryAwareJacksonModule );

		final Field modulesField = ReflectionUtils.findField( RepositoryAwareMappingHttpMessageConverter.class, "modules" );
		ReflectionUtils.makeAccessible( modulesField );

		List<Module> modules = newArrayList();
		modules.add( repositoryAwareJacksonModule );

		ReflectionUtils.setField( modulesField, repositoryAwareMappingHttpMessageConverter, modules );

		final Field mapperField = ReflectionUtils.findField( RepositoryAwareMappingHttpMessageConverter.class, "mapper" );

		ReflectionUtils.makeAccessible( mapperField );

		ReflectionUtils.setField( mapperField, repositoryAwareMappingHttpMessageConverter, new ObjectMapper(  ) );

		try {
			repositoryAwareMappingHttpMessageConverter.afterPropertiesSet();
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	private RepositoryAwareJacksonModule repositoryAwareJacksonModule() {
		final RepositoryAwareJacksonModule repositoryAwareJacksonModule = ( RepositoryAwareJacksonModule ) autowireCapableBeanFactory.autowire( RepositoryAwareJacksonModule.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false );
		try {
			repositoryAwareJacksonModule.afterPropertiesSet();
		} catch ( Exception e ) {
		}
		return repositoryAwareJacksonModule;
	}

	@Override
	public void removeDomainTypeAdministrationConfiguration( final Class<?> domainType ) {
		globalAdministrationConfiguration.removeDomainTypeConfiguration( domainType );
	}

	@Override
	public void removeAllDomainTypeAdministrationConfigurations() {
		globalAdministrationConfiguration.removeAllDomainTypeAdministrationConfigurations();
	}

	@Override
	public void setBeanFactory( final BeanFactory beanFactory ) throws BeansException {
		 this.autowireCapableBeanFactory = ( AutowireCapableBeanFactory ) beanFactory;
	}
}