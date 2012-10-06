package org.lightadmin.core.persistence.metamodel;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class JpaDomainTypeEntityMetadata implements DomainTypeEntityMetadata<JpaDomainTypeAttributeMetadata> {

	private final Class<?> domainType;

	private final JpaDomainTypeAttributeMetadata idAttribute;
	private final JpaDomainTypeAttributeMetadata nameAttribute;
	private final JpaDomainTypeAttributeMetadata versionAttribute;

	private final Map<String, JpaDomainTypeAttributeMetadata> attributes = newHashMap();

	public JpaDomainTypeEntityMetadata( EntityType<?> entityType, String nameFieldName ) {
		domainType = entityType.getJavaType();

		idAttribute = idAttribute( entityType );
		versionAttribute = versionAttribute( entityType );
		nameAttribute = nameAttribute( entityType, nameFieldName );

		for ( Attribute attribute : entityType.getAttributes() ) {
			final Field field = ReflectionUtils.findField( domainType, attribute.getJavaMember().getName() );
			if ( null == field ) {
				continue;
			}

			if ( notIdAttribute( attribute ) && notVersionAttribute( attribute ) && notNameAttribute( attribute ) ) {
				attributes.put( attribute.getName(), new JpaDomainTypeAttributeMetadata( entityType, attribute ) );
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
	public Collection<JpaDomainTypeAttributeMetadata> getAttributes() {
		return attributes.values();
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
	public JpaDomainTypeAttributeMetadata getNameAttribute() {
		return nameAttribute;
	}

	@Override
	public JpaDomainTypeAttributeMetadata getAttribute( String name ) {
		if ( idAttribute.getName().equals( name ) ) {
			return idAttribute;
		}

		if ( null != versionAttribute && versionAttribute.getName().equals( name ) ) {
			return versionAttribute;
		}

		if ( null != nameAttribute && nameAttribute.getName().equals( name ) ) {
			return nameAttribute;
		}

		if ( attributes.containsKey( name ) ) {
			return attributes.get( name );
		}

		return null;
	}

	private boolean notVersionAttribute( final Attribute attribute ) {
		return !( attribute instanceof SingularAttribute && ( ( SingularAttribute ) attribute ).isVersion() );
	}

	private boolean notIdAttribute( final Attribute attribute ) {
		return !( attribute instanceof SingularAttribute && ( ( SingularAttribute ) attribute ).isId() );
	}

	private boolean notNameAttribute( final Attribute attribute ) {
		return !( nameAttribute != null && attribute instanceof SingularAttribute && nameAttribute.getName().equals( attribute.getName() ) );
	}

	private JpaDomainTypeAttributeMetadata nameAttribute( final EntityType<?> entityType, String nameFieldName ) {
		final Attribute<?, ?> attribute = entityType.getAttribute( nameFieldName );
		if ( attribute == null || !( attribute instanceof SingularAttribute ) ) {
			return null;
		}

		final Field field = ReflectionUtils.findField( domainType, attribute.getJavaMember().getName() );
		if ( null == field ) {
			return null;
		}

		return new JpaDomainTypeAttributeMetadata( entityType, attribute );
	}

	private JpaDomainTypeAttributeMetadata versionAttribute( final EntityType<?> entityType ) {
		return entityType.getVersion( Long.class ) != null ? new JpaDomainTypeAttributeMetadata( entityType, entityType.getVersion( Long.class ) ) : null;
	}

	private JpaDomainTypeAttributeMetadata idAttribute( final EntityType<?> entityType ) {
		return new JpaDomainTypeAttributeMetadata( entityType, entityType.getId( entityType.getIdType().getJavaType() ) );
	}
}