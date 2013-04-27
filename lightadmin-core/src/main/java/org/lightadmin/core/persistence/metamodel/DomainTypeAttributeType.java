package org.lightadmin.core.persistence.metamodel;

import javax.persistence.metamodel.Attribute;
import java.util.Date;

import static org.lightadmin.core.util.NumberUtils.isNumberFloat;
import static org.lightadmin.core.util.NumberUtils.isNumberInteger;
public enum DomainTypeAttributeType {

	STRING,
	NUMBER_INTEGER,
	NUMBER_FLOAT,
	BOOL,
	DATE,
	FILE,
	ASSOC,
	ASSOC_MULTI,
	EMBEDDED,
	MAP,
	UNKNOWN;

	public static boolean isSupportedAttributeType( DomainTypeAttributeType attributeType ) {
		return attributeType != UNKNOWN;
	}

	public static DomainTypeAttributeType forType( Class<?> attrType ) {
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

		if ( byte[].class.equals( attrType ) ) {
			return DomainTypeAttributeType.FILE;
		}

		return DomainTypeAttributeType.UNKNOWN;
	}

	public static DomainTypeAttributeType forAttributeMetadata( DomainTypeAttributeMetadata attributeMetadata ) {
		final Class<?> attrType = attributeMetadata.getType();

		if ( attributeMetadata.isAssociation() || attributeMetadata.getAttribute().getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ) {
			if ( attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike() ) {
				return DomainTypeAttributeType.ASSOC_MULTI;
			}
			return DomainTypeAttributeType.ASSOC;
		}

		if ( Attribute.PersistentAttributeType.EMBEDDED == attributeMetadata.getAttribute().getPersistentAttributeType() ) {
			return DomainTypeAttributeType.EMBEDDED;
		}

		if ( attributeMetadata.isMapLike() ) {
			return DomainTypeAttributeType.MAP;
		}

		return forType( attrType );
	}
}