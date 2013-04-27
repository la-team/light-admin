package org.lightadmin.core.rest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.Persistable;
import org.lightadmin.core.config.domain.field.evaluator.FieldValueEvaluator;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.rest.webmvc.EntityResource;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Collections.EMPTY_SET;
import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.addPrimaryKeyPersistentField;

@SuppressWarnings("unchecked")
public class DomainTypeToResourceConverter extends DomainTypeResourceSupport implements Converter<Object, Resource> {

	private final FieldValueEvaluator fieldValueEvaluator = new FieldValueEvaluator();

	private GlobalAdministrationConfiguration configuration;

	public DomainTypeToResourceConverter( GlobalAdministrationConfiguration configuration, RepositoryRestConfiguration restConfiguration ) {
		super( restConfiguration );
		this.configuration = configuration;
	}

	public Resource convert( final Object source, Set<FieldMetadata> fieldMetadatas ) {
		if ( source == null ) {
			return null;
		}

		final DomainTypeBasicConfiguration domainTypeConfiguration = configuration.forDomainType( source.getClass() );
		if ( domainTypeConfiguration == null ) {
			return new Resource<Object>( source );
		}

		final DomainTypeEntityMetadata entityMetadata = domainTypeConfiguration.getDomainTypeEntityMetadata();

		Serializable id = ( Serializable ) entityMetadata.getIdAttribute().getValue( source );

		final EntityResource entityResource = newEntityResource( domainTypeConfiguration.getDomainTypeName(), id );

		addObjectStringRepresentation( entityResource, domainTypeConfiguration, source );

		addManagedTypeProperty( entityResource, source );

		for ( FieldMetadata field : fieldMetadatas ) {
			addFieldAttributeValue( entityResource, field, source, domainTypeConfiguration, id );
		}

		return entityResource;
	}

	@Override
	public Resource convert( Object source ) {
		if ( source == null ) {
			return null;
		}

		final DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forManagedDomainType( source.getClass() );
		final DomainTypeBasicConfiguration domainTypeBasicConfig = configuration.forDomainType( source.getClass() );

		if ( domainTypeConfiguration != null ) {
			return convert( source, primaryKeyFieldOnly( domainTypeConfiguration ) );
		}

		if ( domainTypeBasicConfig != null ) {
			return convert( source, primaryKeyFieldOnly( domainTypeBasicConfig ) );
		}

		return new Resource<Object>( source );
	}

	private Set<FieldMetadata> primaryKeyFieldOnly( DomainTypeBasicConfiguration domainTypeBasicConfig ) {
		return addPrimaryKeyPersistentField( EMPTY_SET, domainTypeBasicConfig.getDomainTypeEntityMetadata().getIdAttribute() );
	}

	private void addObjectStringRepresentation( final EntityResource resource, final DomainTypeBasicConfiguration configuration, final Object source ) {
		final EntityNameExtractor nameExtractor = configuration.getEntityConfiguration().getNameExtractor();

		resource.getContent().put( "stringRepresentation", exceptionAwareNameExtractor( nameExtractor ).apply( source ) );
	}

	private Object addManagedTypeProperty( final EntityResource entityResource, final Object source ) {
		return entityResource.getContent().put( "managedDomainType", configuration.forManagedDomainType( source.getClass() ) != null );
	}

	private void addFieldAttributeValue( EntityResource resource, FieldMetadata field, Object source, final DomainTypeBasicConfiguration domainTypeConfiguration, final Serializable id ) {
		Map<String, Object> fieldData = newLinkedHashMap();
		final String fieldName = field.getName();
		final Object fieldValue = fieldValueEvaluator.evaluate( field, source );
		final DomainTypeAttributeType attributeType = domainTypeAttributeType( field, fieldValue );

		fieldData.put( "name", fieldName );
		fieldData.put( "value", fieldValue );
		fieldData.put( "type", attributeType.name() );

		fieldData.put( "primaryKey", ( field instanceof Persistable ) && ( ( Persistable ) field ).isPrimaryKey() );

		fieldData.put( "propertyLink", propertyLink( field, domainTypeConfiguration.getDomainTypeName(), id ) );

		addAttributeValue( resource, field.getUuid(), fieldData );
	}

	private String propertyLink( final FieldMetadata field, final String domainTypeName, final Serializable id ) {
		if ( !( field instanceof Persistable ) ) {
			return null;
		}

		final Persistable persistableField = ( Persistable ) field;
		final DomainTypeAttributeType attributeType = persistableField.getAttributeMetadata().getAttributeType();

		UriComponentsBuilder propertyUriBuilder = UriComponentsBuilder.fromUri( restConfiguration.getBaseUri() ).pathSegment( domainTypeName ).pathSegment( id.toString() ).pathSegment( persistableField.getField() );
		if ( DomainTypeAttributeType.FILE == attributeType ) {
			propertyUriBuilder.pathSegment( "file" );
		}

		return propertyUriBuilder.build().toUri().toString();
	}

	private DomainTypeAttributeType domainTypeAttributeType( final FieldMetadata field, final Object fieldValue ) {
		if ( field instanceof Persistable ) {
			return ( ( Persistable ) field ).getAttributeMetadata().getAttributeType();
		}
		return fieldValue != null ? DomainTypeAttributeType.forType( fieldValue.getClass() ) : DomainTypeAttributeType.UNKNOWN;
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