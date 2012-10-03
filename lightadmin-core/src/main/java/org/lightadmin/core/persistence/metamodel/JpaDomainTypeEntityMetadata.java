package org.lightadmin.core.persistence.metamodel;

import org.springframework.beans.BeanUtils;
import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class JpaDomainTypeEntityMetadata implements DomainTypeEntityMetadata<JpaDomainTypeAttributeMetadata> {

	private final Class<?> domainType;

	private final JpaDomainTypeAttributeMetadata idAttribute;
	private final JpaDomainTypeAttributeMetadata versionAttribute;

	private final Map<String, JpaDomainTypeAttributeMetadata> embeddedAttributes = newHashMap();
	private final Map<String, JpaDomainTypeAttributeMetadata> linkedAttributes = newHashMap();

	public JpaDomainTypeEntityMetadata( EntityType<?> entityType ) {
		domainType = entityType.getJavaType();

		idAttribute = idAttribute( entityType );
		versionAttribute = versionAttribute( entityType );

		for ( Attribute attribute : entityType.getAttributes() ) {
			final Field field = ReflectionUtils.findField( domainType, attribute.getJavaMember().getName() );
			if ( null == field ) {
				continue;
			}

			final String attributeName = attribute.getName();

			if ( !BeanUtils.isSimpleValueType( attributeType( attribute ) ) ) {
				linkedAttributes.put( attributeName, new JpaDomainTypeAttributeMetadata( new JpaAttributeMetadata( entityType, attribute ) ) );
				continue;
			}

			if ( notIdAttribute( attribute ) && notVersionAttribute( attribute ) ) {
				embeddedAttributes.put( attributeName, new JpaDomainTypeAttributeMetadata( new JpaAttributeMetadata( entityType, attribute ) ) );
			}
		}
	}

	@Override
	public Class<?> getDomainType() {
		return domainType;
	}

	public String getEntityName() {
		Entity entity = domainType.getAnnotation( Entity.class );
		boolean hasName = null != entity && StringUtils.hasText( entity.name() );

		return hasName ? entity.name() : domainType.getSimpleName();
	}

	@Override
	public Map<String, JpaDomainTypeAttributeMetadata> getEmbeddedAttributes() {
		return embeddedAttributes;
	}

	@Override
	public Map<String, JpaDomainTypeAttributeMetadata> getLinkedAttributes() {
		return linkedAttributes;
	}

	@Override
	public Map<String, JpaDomainTypeAttributeMetadata> getAttributes() {
		Map<String, JpaDomainTypeAttributeMetadata> attributes = newHashMap();
		attributes.putAll( embeddedAttributes );
		attributes.putAll( linkedAttributes );
		return attributes;
	}

	@Override
	public JpaDomainTypeAttributeMetadata getVersionAttribute() {
		return versionAttribute;
	}

	@Override
	public JpaDomainTypeAttributeMetadata getIdAttribute() {
		return idAttribute;
	}

	@Override
	public JpaDomainTypeAttributeMetadata getAttribute( String name ) {
		if ( idAttribute.getName().equals( name ) ) {
			return idAttribute;
		}

		if ( null != versionAttribute && versionAttribute.getName().equals( name ) ) {
			return versionAttribute;
		}

		if ( embeddedAttributes.containsKey( name ) ) {
			return embeddedAttributes.get( name );
		}

		if ( linkedAttributes.containsKey( name ) ) {
			return linkedAttributes.get( name );
		}

		return null;
	}

	private Class attributeType( final Attribute attribute ) {
		return ( attribute instanceof PluralAttribute ? ( ( PluralAttribute ) attribute ).getElementType().getJavaType() : attribute.getJavaType() );
	}

	private boolean notVersionAttribute( final Attribute attribute ) {
		return !( attribute instanceof SingularAttribute && ( ( SingularAttribute ) attribute ).isVersion() );
	}

	private boolean notIdAttribute( final Attribute attribute ) {
		return !( attribute instanceof SingularAttribute && ( ( SingularAttribute ) attribute ).isId() );
	}

	private JpaDomainTypeAttributeMetadata versionAttribute( final EntityType<?> entityType ) {
		return entityType.getVersion( Long.class ) != null ? new JpaDomainTypeAttributeMetadata( new JpaAttributeMetadata( entityType, entityType.getVersion( Long.class ) ) ) : null;
	}

	private JpaDomainTypeAttributeMetadata idAttribute( final EntityType<?> entityType ) {
		return new JpaDomainTypeAttributeMetadata( new JpaAttributeMetadata( entityType, entityType.getId( entityType.getIdType().getJavaType() ) ) );
	}
}