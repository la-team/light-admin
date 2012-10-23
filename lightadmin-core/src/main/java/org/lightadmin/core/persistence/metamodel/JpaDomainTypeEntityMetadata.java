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

	private final Map<String, JpaDomainTypeAttributeMetadata> attributes = newHashMap();

	public JpaDomainTypeEntityMetadata( EntityType<?> entityType ) {
		domainType = entityType.getJavaType();

		idAttribute = idAttribute( entityType );

		for ( Attribute attribute : entityType.getAttributes() ) {
			final Field field = ReflectionUtils.findField( domainType, attribute.getJavaMember().getName() );
			if ( null == field ) {
				continue;
			}

			if ( notIdAttribute( attribute ) ) {
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
	public JpaDomainTypeAttributeMetadata getIdAttribute() {
		return idAttribute;
	}

	@Override
	public JpaDomainTypeAttributeMetadata getAttribute( String name ) {
		if ( idAttribute.getName().equals( name ) ) {
			return idAttribute;
		}

		if ( attributes.containsKey( name ) ) {
			return attributes.get( name );
		}

		return null;
	}

	@Override
	public boolean containsAttribute( final String name ) {
		return getAttribute( name ) != null;
	}

	private boolean notIdAttribute( final Attribute attribute ) {
		return !( attribute instanceof SingularAttribute && ( ( SingularAttribute ) attribute ).isId() );
	}

	private JpaDomainTypeAttributeMetadata idAttribute( final EntityType<?> entityType ) {
		return new JpaDomainTypeAttributeMetadata( entityType, entityType.getId( entityType.getIdType().getJavaType() ) );
	}
}