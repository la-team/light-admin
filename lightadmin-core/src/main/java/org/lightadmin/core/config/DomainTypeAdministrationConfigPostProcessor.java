package org.lightadmin.core.config;

import org.lightadmin.core.view.support.Filter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DomainTypeAdministrationConfigPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization( final Object bean, final String beanName ) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization( final Object bean, final String beanName ) throws BeansException {
		if ( DomainTypeAdministrationConfiguration.class.isAssignableFrom( bean.getClass() ) ) {
			validateConfiguration( ( DomainTypeAdministrationConfiguration ) bean, beanName );
			configurationPostInitialization( ( DomainTypeAdministrationConfiguration ) bean, beanName );
		}
		return bean;
	}

	private void validateConfiguration( final DomainTypeAdministrationConfiguration bean, final String beanName ) throws BeanCreationException {
		//TODO: max: Configuration validation goes here ;)
	}

	private void configurationPostInitialization( final DomainTypeAdministrationConfiguration configuration, final String configurationName ) throws BeanCreationException {
		for ( Filter filter : configuration.getFilters() ) {
			if ( !configuration.getEntityMetadata().embeddedAttributes().containsKey( filter.getFieldName() ) ) {
				throw new BeanCreationException( configurationName, String.format( "Incorrect field name '%s' specified for filtering!", filter.getFieldName() ) );
			}
			filter.setAttributeMetadata( configuration.getEntityMetadata().embeddedAttributes().get( filter.getFieldName() ) );
		}
	}
}