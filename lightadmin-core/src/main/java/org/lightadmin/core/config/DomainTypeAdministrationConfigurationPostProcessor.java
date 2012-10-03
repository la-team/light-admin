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

public class DomainTypeAdministrationConfigurationPostProcessor implements BeanPostProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Object postProcessBeforeInitialization( final Object bean, final String beanName ) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization( final Object bean, final String beanName ) throws BeansException {
		if ( DomainTypeAdministrationConfiguration.class.isAssignableFrom( bean.getClass() ) ) {
			configurationPostInitialization( ( DomainTypeAdministrationConfiguration ) bean, beanName );
		}
		return bean;
	}

	private void configurationPostInitialization( final DomainTypeAdministrationConfiguration configuration, final String configurationName ) throws BeanCreationException {
		DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> entityMetadata = domainTypeMetadata( configuration.getDomainType() );

		configuration.setDomainTypeEntityMetadata( entityMetadata );

		for ( Filter filter : configuration.getFilters() ) {
			final DomainTypeAttributeMetadata attributeMetadata = entityMetadata.getAttribute( filter.getFieldName() );
			if ( attributeMetadata == null ) {
				throw new BeanCreationException( configurationName, String.format( "Incorrect field name '%s' specified for filtering!", filter.getFieldName() ) );
			}
			filter.setAttributeMetadata( attributeMetadata );
		}
	}

	private JpaDomainTypeEntityMetadata domainTypeMetadata( final Class<?> domainType ) {
		return new JpaDomainTypeEntityMetadata( entityManager.getMetamodel().entity( domainType ) );
	}
}