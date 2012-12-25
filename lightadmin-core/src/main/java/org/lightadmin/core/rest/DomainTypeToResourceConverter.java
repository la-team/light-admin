package org.lightadmin.core.rest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.Persistable;
import org.lightadmin.core.config.domain.field.evaluator.FieldValueEvaluator;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.rest.webmvc.EntityResource;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;

@SuppressWarnings( "unchecked" )
public class DomainTypeToResourceConverter extends DomainTypeResourceSupport implements Converter<Object, Resource> {

	private GlobalAdministrationConfiguration configuration;

	private FieldValueEvaluator fieldValueEvaluator = new FieldValueEvaluator();

	public DomainTypeToResourceConverter( GlobalAdministrationConfiguration configuration, RepositoryRestConfiguration restConfiguration ) {
		super(restConfiguration);
		this.configuration = configuration;
	}

	public Resource convert( final Object source, Set<FieldMetadata> fieldMetadatas ) {
		if ( source == null ) {
			return new Resource<Object>( source );
		}

		final DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forDomainType( source.getClass() );
		if ( domainTypeConfiguration == null ) {
			return new Resource<Object>( source );
		}

		final DomainTypeEntityMetadata entityMetadata = domainTypeConfiguration.getDomainTypeEntityMetadata();

		Serializable id = (Serializable) entityMetadata.getIdAttribute().getValue( source );

		final EntityResource entityResource = newEntityResource( domainTypeConfiguration.getDomainTypeName(), id );

		addObjectStringRepresentation( entityResource, domainTypeConfiguration, source );

		for ( FieldMetadata field : fieldMetadatas ) {
			addFieldAttributeValue( entityResource, field, source );
		}

		return entityResource;
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

		return convert( source, fieldsForShowView( domainTypeConfiguration ) );
	}

	private Set<FieldMetadata> fieldsForShowView( final DomainTypeAdministrationConfiguration domainTypeConfiguration ) {
		return domainTypeConfiguration.getShowViewFragment().getFields();
	}

	private void addObjectStringRepresentation( final EntityResource resource, final DomainTypeAdministrationConfiguration configuration, final Object source ) {
		resource.getContent().put( "stringRepresentation", configuration.getEntityConfiguration().getNameExtractor().apply( source ) );
	}

	private void addFieldAttributeValue( EntityResource resource, FieldMetadata field, Object source ) {
		Map<String, Object> fieldData = newLinkedHashMap();
		fieldData.put( "name", field.getName() );
		fieldData.put( "value", fieldValueEvaluator.evaluate( field, source ) );
		fieldData.put( "primaryKey", ( field instanceof Persistable ) && ( ( Persistable ) field ).isPrimaryKey() );

		addAttributeValue( resource, field.getUuid(), fieldData );
	}

	private void addAttributeValue( EntityResource resource, String attributeName, Object value ) {
		if ( value != null ) {
			resource.getContent().put( attributeName, value );
		}
	}

	private EntityResource newEntityResource( String domainTypeName, Serializable id ) {
		final HashSet<Link> links = Sets.newLinkedHashSet();
		links.add( selfLink( domainTypeName, id ) );
		links.add( selfDomainLink( domainTypeName, id ) );

		return new EntityResource( Maps.<String, Object>newLinkedHashMap(), links );
	}

}
