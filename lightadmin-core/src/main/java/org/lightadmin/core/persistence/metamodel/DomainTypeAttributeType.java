package org.lightadmin.core.persistence.metamodel;

public enum DomainTypeAttributeType {

	STRING,
	NUMBER_INTEGER,
	NUMBER_FLOAT,
	BOOL,
	DATE,
	ASSOC,
	ASSOC_MULTI,
	EMBEDDED,
	MAP,
	UNKNOWN;

	public static boolean isSupportedAttributeType( DomainTypeAttributeType attributeType ) {
		return attributeType != UNKNOWN;
	}
}