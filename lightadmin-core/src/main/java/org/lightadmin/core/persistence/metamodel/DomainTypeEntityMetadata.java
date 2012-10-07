package org.lightadmin.core.persistence.metamodel;

import java.util.Collection;

public interface DomainTypeEntityMetadata<A extends DomainTypeAttributeMetadata> {

	Class<?> getDomainType();

	String getEntityName();

	Collection<A> getAttributes();

	A getAttribute( String name );

	A getIdAttribute();

}