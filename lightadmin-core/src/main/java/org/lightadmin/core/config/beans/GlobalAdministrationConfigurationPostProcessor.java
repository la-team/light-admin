package org.lightadmin.core.config.beans;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.filter.Filter;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class GlobalAdministrationConfigurationPostProcessor implements BeanPostProcessor {

	@Autowired
	private DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> entityMetadataResolver;

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
			configurationPostInitialization( domainTypeAdministrationConfiguration );
		}
	}

	@SuppressWarnings( "unchecked" )
	private void configurationPostInitialization( final DomainTypeAdministrationConfiguration configuration ) throws BeanCreationException {
		DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> entityMetadata = entityMetadataResolver.resolveEntityMetadata( configuration.getDomainType() );

		configuration.setDomainTypeEntityMetadata( entityMetadata );

		for ( Filter filter : configuration.getFilters() ) {
			final DomainTypeAttributeMetadata attributeMetadata = entityMetadata.getAttribute( filter.getFieldName() );
			if ( attributeMetadata == null ) {
				throw new BeanCreationException( configuration.getClass().getSimpleName(), String.format( "Incorrect field name '%s' specified for filtering!", filter.getFieldName() ) );
			}
			filter.setAttributeMetadata( attributeMetadata );
		}
	}
}