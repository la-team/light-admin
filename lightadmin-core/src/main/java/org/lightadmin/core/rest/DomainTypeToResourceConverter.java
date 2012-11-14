package org.lightadmin.core.rest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.evaluator.FieldValueEvaluator;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.rest.webmvc.EntityResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.Set;

@SuppressWarnings( "unchecked" )
public class DomainTypeToResourceConverter implements Converter<Object, Resource> {

	private GlobalAdministrationConfiguration configuration;

	private FieldValueEvaluator fieldValueEvaluator = new FieldValueEvaluator();

	public DomainTypeToResourceConverter( GlobalAdministrationConfiguration configuration ) {
		this.configuration = configuration;
	}

	@Override
	public Resource convert( final Object source ) {
		if ( source == null ) {
			return new Resource<Object>( source );
		}

		final DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forDomainType( source.getClass() );
		if ( domainTypeConfiguration == null ) {
			return new Resource<Object>( source );
		}

		final DomainTypeEntityMetadata entityMetadata = domainTypeConfiguration.getDomainTypeEntityMetadata();

		final EntityResource entityResource = emptyEntityResource();

		addAttribute( entityResource, entityMetadata.getIdAttribute(), source );

		addObjectStringRepresentation( entityResource, domainTypeConfiguration, source );

		for ( FieldMetadata field : fieldsForListView( domainTypeConfiguration ) ) {
			addAttributeValue( entityResource, field.getUuid(), fieldValueEvaluator.evaluate( field, source ) );
		}

		return entityResource;
	}

	private Set<FieldMetadata> fieldsForListView( final DomainTypeAdministrationConfiguration domainTypeConfiguration ) {
		return domainTypeConfiguration.getListViewFragment().getFields();
	}

	private void addObjectStringRepresentation( final EntityResource resource, final DomainTypeAdministrationConfiguration configuration, final Object source ) {
		resource.getContent().put( "stringRepresentation", configuration.getEntityConfiguration().getNameExtractor().apply( source ) );
	}

	private void addAttributeValue( EntityResource resource, String attributeName, Object value ) {
		if ( value != null ) {
			resource.getContent().put( attributeName, value );
		}
	}

	private void addAttribute( EntityResource resource, DomainTypeAttributeMetadata attributeMetadata, Object source ) {
		addAttributeValue( resource, attributeMetadata.getName(), attributeMetadata.getValue( source ) );
	}

	private EntityResource emptyEntityResource() {
		return new EntityResource( Maps.<String, Object>newHashMap(), Sets.<Link>newHashSet() );
	}
}