package org.lightadmin.core.view.tags.form;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import javax.persistence.metamodel.Attribute;
import java.util.Date;

// max: Looks like we could use it in few other places, i.e. SpecificationCreator,...
enum DomainTypeAttributeType {

	BASIC,
	BOOL,
	DATE,
	ASSOC,
	ASSOC_MULTI,
	MAP,
	UNKNOWN;

	public static DomainTypeAttributeType by(DomainTypeAttributeMetadata attrMeta) {
		Class<?> attrType = attrMeta.getType();
		if (attrMeta.isAssociation() || attrMeta.getAttribute().getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ) {
			if (attrMeta.isCollectionLike() || attrMeta.isSetLike()) {
				return ASSOC_MULTI;
			} else {
				return ASSOC;
			}
		} else if (attrMeta.isMapLike()) {
			return MAP;
		} else if (Boolean.class.equals( attrType ) || boolean.class.equals( attrType )) {
			return BOOL;
		} else if (Date.class.isAssignableFrom( attrType )) {
			return DATE;
		} else if (String.class.equals(attrType) || Number.class.isAssignableFrom(attrType)) {
			return BASIC;
		} else {
			return UNKNOWN;
		}
	}

}
