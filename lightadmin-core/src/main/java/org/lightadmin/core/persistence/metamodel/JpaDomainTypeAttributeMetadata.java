package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.lang.annotation.Annotation;
import java.util.Date;

import static javax.persistence.metamodel.Attribute.PersistentAttributeType.EMBEDDED;
import static org.lightadmin.core.util.NumberUtils.isNumberFloat;
import static org.lightadmin.core.util.NumberUtils.isNumberInteger;

public class JpaDomainTypeAttributeMetadata implements DomainTypeAttributeMetadata {

	private JpaAttributeMetadata attributeMetadata;

	private Attribute attribute;

	public JpaDomainTypeAttributeMetadata( JpaAttributeMetadata attributeMetadata ) {
		this.attributeMetadata = attributeMetadata;
	}

	public JpaDomainTypeAttributeMetadata( EntityType<?> entityType, Attribute attribute ) {
		this( new JpaAttributeMetadata( entityType, attribute ) );
		this.attribute = attribute;
	}

	@Override
	public boolean hasAnnotation( final Class<? extends Annotation> annoType ) {
		return attributeMetadata.hasAnnotation( annoType );
	}

	@Override
	public <A extends Annotation> A annotation( final Class<A> annoType ) {
		return attributeMetadata.annotation( annoType );
	}

	@Override
	public String getName() {
		return attributeMetadata.name();
	}

	@Override
	public Class<?> getType() {
		return attributeMetadata.type();
	}

	@Override
	public Class<?> getElementType() {
		return attributeMetadata.elementType();
	}

	@Override
	public boolean isCollectionLike() {
		return attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike();
	}

	@Override
	public boolean isSetLike() {
		return attributeMetadata.isSetLike();
	}

	@Override
	public boolean isMapLike() {
		return attributeMetadata.isMapLike();
	}

	@Override
	public boolean isAssociation() {
		switch ( attribute.getPersistentAttributeType() ) {
			case ONE_TO_ONE:
			case ONE_TO_MANY:
			case MANY_TO_ONE:
			case MANY_TO_MANY:
				return true;
			default:
				return false;
		}
	}

	@Override
	public Object getValue( final Object target ) {
		return attributeMetadata.get( target );
	}

	public Attribute getAttribute() {
		return attribute;
	}

	@Override
	public Class<?> getAssociationDomainType() {
		if ( getAttribute().isCollection() ) {
			return getElementType();
		}
		return getType();
	}

	@Override
	public DomainTypeAttributeType getAttributeType() {
		final Class<?> attrType = getType();

		if ( isAssociation() || getAttribute().getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ) {
			if ( isCollectionLike() || isSetLike() ) {
				return DomainTypeAttributeType.ASSOC_MULTI;
			}
			return DomainTypeAttributeType.ASSOC;
		}

		if ( isMapLike() ) {
			return DomainTypeAttributeType.MAP;
		}

		if ( Boolean.class.equals( attrType ) || boolean.class.equals( attrType ) ) {
			return DomainTypeAttributeType.BOOL;
		}

		if ( Date.class.isAssignableFrom( attrType ) ) {
			return DomainTypeAttributeType.DATE;
		}

		if ( isNumberInteger( attrType ) ) {
			return DomainTypeAttributeType.NUMBER_INTEGER;
		}

		if ( isNumberFloat( attrType ) ) {
			return DomainTypeAttributeType.NUMBER_FLOAT;
		}

		if ( String.class.equals( attrType ) ) {
			return DomainTypeAttributeType.STRING;
		}

		if ( EMBEDDED == attribute.getPersistentAttributeType() ) {
			return DomainTypeAttributeType.EMBEDDED;
		}

		return DomainTypeAttributeType.UNKNOWN;
	}
}