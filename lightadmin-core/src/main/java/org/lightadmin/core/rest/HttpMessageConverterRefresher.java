package org.lightadmin.core.rest;

import com.google.common.collect.Lists;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
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

public class HttpMessageConverterRefresher implements BeanFactoryAware {

	@Autowired
	private RepositoryAwareMappingHttpMessageConverter repositoryAwareMappingHttpMessageConverter;

	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	public void refresh() {
		final RepositoryAwareJacksonModule repositoryAwareJacksonModule = repositoryAwareJacksonModule();

		setJacksonModuleFieldValue( repositoryAwareJacksonModule );

		setModulesFieldValue( repositoryAwareJacksonModule );

		setObjectMapperFieldValue( new ObjectMapper() );

		refreshConverterConfiguration();
	}

	private void refreshConverterConfiguration() {
		try {
			repositoryAwareMappingHttpMessageConverter.afterPropertiesSet();
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	private void setObjectMapperFieldValue( final ObjectMapper objectMapper ) {
		final Field mapperField = ReflectionUtils.findField( RepositoryAwareMappingHttpMessageConverter.class, "mapper" );
		ReflectionUtils.makeAccessible( mapperField );
		ReflectionUtils.setField( mapperField, repositoryAwareMappingHttpMessageConverter, objectMapper );
	}

	private void setModulesFieldValue( final RepositoryAwareJacksonModule repositoryAwareJacksonModule ) {
		List<Module> modules = Lists.newArrayList();
		modules.add( repositoryAwareJacksonModule );

		final Field modulesField = ReflectionUtils.findField( RepositoryAwareMappingHttpMessageConverter.class, "modules" );
		ReflectionUtils.makeAccessible( modulesField );
		ReflectionUtils.setField( modulesField, repositoryAwareMappingHttpMessageConverter, modules );
	}

	private void setJacksonModuleFieldValue( final RepositoryAwareJacksonModule repositoryAwareJacksonModule ) {
		final Field jacksonModuleField = ReflectionUtils.findField( RepositoryAwareMappingHttpMessageConverter.class, "jacksonModule" );
		ReflectionUtils.makeAccessible( jacksonModuleField );
		ReflectionUtils.setField( jacksonModuleField, repositoryAwareMappingHttpMessageConverter, repositoryAwareJacksonModule );
	}

	private RepositoryAwareJacksonModule repositoryAwareJacksonModule() {
		final RepositoryAwareJacksonModule repositoryAwareJacksonModule = ( RepositoryAwareJacksonModule ) autowireCapableBeanFactory.autowire( RepositoryAwareJacksonModule.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false );
		try {
			repositoryAwareJacksonModule.afterPropertiesSet();
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
		return repositoryAwareJacksonModule;
	}

	@Override
	public void setBeanFactory( final BeanFactory beanFactory ) throws BeansException {
		this.autowireCapableBeanFactory = ( AutowireCapableBeanFactory ) beanFactory;
	}
}