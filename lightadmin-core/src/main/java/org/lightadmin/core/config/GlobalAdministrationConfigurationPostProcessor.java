package org.lightadmin.core.config;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadata;
import org.lightadmin.core.view.support.filter.Filter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GlobalAdministrationConfigurationPostProcessor implements BeanPostProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Object postProcessBeforeInitialization( final Object bean, final String beanName ) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization( final Object bean, final String beanName ) throws BeansException {
		if ( GlobalAdministrationConfiguration.class.isAssignableFrom( bean.getClass() ) ) {
			configurationPostInitialization( ( GlobalAdministrationConfiguration ) bean );
		}
		return bean;
	}

	private void configurationPostInitialization( final GlobalAdministrationConfiguration configuration ) throws BeanCreationException {
		for ( DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration : configuration.getDomainTypeConfigurations().values() ) {
			configurationPostInitialization( domainTypeAdministrationConfiguration, configuration );
		}
	}

	private void configurationPostInitialization( final DomainTypeAdministrationConfiguration configuration, final GlobalAdministrationConfiguration globalAdministrationConfiguration ) throws BeanCreationException {
		DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> entityMetadata = domainTypeMetadata( configuration.getDomainType(), configuration.getNameField(), globalAdministrationConfiguration );

		configuration.setDomainTypeEntityMetadata( entityMetadata );

		for ( Filter filter : configuration.getFilters() ) {
			final DomainTypeAttributeMetadata attributeMetadata = entityMetadata.getAttribute( filter.getFieldName() );
			if ( attributeMetadata == null ) {
				throw new BeanCreationException( configuration.getClass().getSimpleName(), String.format( "Incorrect field name '%s' specified for filtering!", filter.getFieldName() ) );
			}
			filter.setAttributeMetadata( attributeMetadata );
		}
	}

	private JpaDomainTypeEntityMetadata domainTypeMetadata( final Class<?> domainType, final String nameField, final GlobalAdministrationConfiguration configuration ) {
		return new JpaDomainTypeEntityMetadata( entityManager.getMetamodel().entity( domainType ), nameField, configuration );
	}
}