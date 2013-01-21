package org.lightadmin.core.rest;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import static java.util.Collections.EMPTY_SET;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.rest.webmvc.EntityResource;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import static com.google.common.collect.Maps.newLinkedHashMap;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.Persistable;
import org.lightadmin.core.config.domain.field.evaluator.FieldValueEvaluator;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.addPrimaryKeyPersistentField;

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

		final DomainTypeBasicConfiguration domainTypeConfiguration = configuration.forDomainType( source.getClass() );
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
	public Resource convert( Object source ) {
		if ( source == null ) {
			return new Resource<Object>( source );
		}

		DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forManagedDomainType( source.getClass() );
		if ( domainTypeConfiguration != null ) {
			return convert( source, fieldsForShowView( domainTypeConfiguration ) );
		}

		DomainTypeBasicConfiguration domainTypeBasicConfig = configuration.forDomainType( source.getClass() );
		if ( domainTypeBasicConfig != null ) {
			return convert( source, fieldsForGenericDomainType( domainTypeBasicConfig ) );
		}

		return new Resource<Object>( source );
	}

	private Set<FieldMetadata> fieldsForGenericDomainType(DomainTypeBasicConfiguration domainTypeBasicConfig) {
		return addPrimaryKeyPersistentField(EMPTY_SET, domainTypeBasicConfig.getDomainTypeEntityMetadata().getIdAttribute());
	}

	private Set<FieldMetadata> fieldsForShowView( DomainTypeAdministrationConfiguration domainTypeConfiguration ) {
		return domainTypeConfiguration.getShowViewFragment().getFields();
	}

	private void addObjectStringRepresentation( final EntityResource resource, final DomainTypeBasicConfiguration configuration, final Object source ) {
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
