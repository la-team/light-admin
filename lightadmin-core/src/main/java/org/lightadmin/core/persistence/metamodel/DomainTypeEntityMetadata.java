package org.lightadmin.core.persistence.metamodel;

import java.util.Map;

public interface DomainTypeEntityMetadata<A extends DomainTypeAttributeMetadata> {

	Class<?> getDomainType();

	String getEntityName();

	Map<String, A> getEmbeddedAttributes();

	Map<String, A> getLinkedAttributes();

	Map<String, A> getAttributes();

	A getAttribute( String name );

	A getIdAttribute();

	A getVersionAttribute();

}