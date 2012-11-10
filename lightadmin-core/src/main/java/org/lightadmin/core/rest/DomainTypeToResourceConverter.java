package org.lightadmin.core.rest;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.fragment.FieldMetadata;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.rest.repository.RepositoryExporter;
import org.springframework.data.rest.repository.RepositoryMetadata;
import org.springframework.data.rest.webmvc.EntityResource;
import org.springframework.data.rest.webmvc.EntityToResourceConverter;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.hateoas.Resource;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Lists.newLinkedList;

@SuppressWarnings( "unchecked" )
public class DomainTypeToResourceConverter implements Converter<Object, Resource> {

	private RepositoryExporter repositoryExporter;

	private RepositoryRestConfiguration repositoryRestConfiguration;

	private GlobalAdministrationConfiguration configuration;

	public DomainTypeToResourceConverter( final RepositoryRestConfiguration repositoryRestConfiguration,
										  final RepositoryExporter repositoryExporter,
										  GlobalAdministrationConfiguration configuration) {
		this.repositoryRestConfiguration = repositoryRestConfiguration;
		this.repositoryExporter = repositoryExporter;
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

		final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> entityMetadata = domainTypeConfiguration.getDomainTypeEntityMetadata();

		final EntityResource entityResource = convert( domainTypeConfiguration.getDomainType(), source );

		addAttribute( entityResource, entityMetadata.getIdAttribute(), source );

		addObjectStringRepresentation( entityResource, domainTypeConfiguration, source );

		final Collection<DomainTypeAttributeMetadata> listViewAttributes = selectForListView( entityMetadata.getAttributes(), domainTypeConfiguration );

		final Set<FieldMetadata> fields = domainTypeConfiguration.getListViewFragment().getFields();

		for ( FieldMetadata field : fields ) {
			final FieldValueRenderer<Object> fieldValueRenderer = field.getRenderer();
			if ( fieldValueRenderer != null ) {
				addAttributeValue( entityResource, field.getFieldName(), fieldValueRenderer.apply( source ) );
			} else {
				final String attributeName = fieldNameSegment( field, domainTypeConfiguration.getDomainType() );

				final DomainTypeAttributeMetadata attributeMetadata = findByName( attributeName, listViewAttributes );
				addAttribute( entityResource, attributeMetadata, source );
			}
		}

		return entityResource;
	}

	private DomainTypeAttributeMetadata findByName( String attributeName, Collection<DomainTypeAttributeMetadata> attributeMetadatas ) {
		for ( DomainTypeAttributeMetadata attributeMetadata : attributeMetadatas ) {
			if ( attributeMetadata.getName().equals( attributeName )) {
				return attributeMetadata;
			}
		}
		throw new RuntimeException( String.format( "No attribute found for name %s", attributeName ) );
	}

	private String fieldNameSegment( FieldMetadata fieldMetadata, Class<?> domainType ) {
		return PropertyPath.from( fieldMetadata.getFieldName(), domainType ).getSegment();
	}

	private Collection<DomainTypeAttributeMetadata> selectForListView( final Collection<? extends DomainTypeAttributeMetadata> domainTypeAttributeMetadatas, final DomainTypeAdministrationConfiguration configuration ) {
		final Set<FieldMetadata> fields = configuration.getListViewFragment().getFields();

		Collection<DomainTypeAttributeMetadata> result = newLinkedList();
		for ( DomainTypeAttributeMetadata attribute : domainTypeAttributeMetadatas ) {
			for ( FieldMetadata field : fields ) {
				if ( attribute.getName().equals( fieldNameSegment( field, configuration.getDomainType() ) )) {
					result.add( attribute );
					break;
				}
			}
		}
		return result;
	}

	private void addObjectStringRepresentation( final EntityResource resource, final DomainTypeAdministrationConfiguration configuration, final Object source ) {
		resource.getContent().put( "stringRepresentation", configuration.getEntityConfiguration().getNameExtractor().apply( source ) );
	}

	private void addAttributeValue( EntityResource resource, String attributeName, Object value ) {
		resource.getContent().put( attributeName, value );
	}

	private void addAttribute( EntityResource resource, DomainTypeAttributeMetadata attributeMetadata, Object source ) {
		final Object attributeValue = attributeMetadata.getValue( source );
		if ( attributeValue != null ) {
			resource.getContent().put( attributeMetadata.getName(), attributeValue );
		}
	}

	private EntityResource convert( Class<?> domainType, Object source ) {
		return ( EntityResource ) entityToResourceConverter( domainType ).convert( source );
	}

	private EntityToResourceConverter entityToResourceConverter( final Class domainType ) {
		final RepositoryMetadata repositoryMetadata = repositoryExporter.repositoryMetadataFor( domainType );

		return new EntityToResourceConverter( repositoryRestConfiguration, repositoryMetadata );
	}
}