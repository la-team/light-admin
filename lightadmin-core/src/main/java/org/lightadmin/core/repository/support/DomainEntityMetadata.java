package org.lightadmin.core.repository.support;

import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.repository.EntityMetadata;
import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;
import org.springframework.data.rest.repository.jpa.JpaEntityMetadata;

import javax.persistence.metamodel.EntityType;
import java.util.Map;

public class DomainEntityMetadata implements EntityMetadata<JpaAttributeMetadata> {

	private final JpaEntityMetadata jpaEntityMetadata;

	public DomainEntityMetadata( EntityType<?> entityType ) {
		jpaEntityMetadata = new JpaEntityMetadata( emptyRepositories(), entityType );
	}

	@Override
	public Class<?> type() {
		return jpaEntityMetadata.type();
	}

	@Override
	public Map<String, JpaAttributeMetadata> embeddedAttributes() {
		return jpaEntityMetadata.embeddedAttributes();
	}

	@Override
	public Map<String, JpaAttributeMetadata> linkedAttributes() {
		return jpaEntityMetadata.linkedAttributes();
	}

	@Override
	public JpaAttributeMetadata idAttribute() {
		return jpaEntityMetadata.idAttribute();
	}

	@Override
	public JpaAttributeMetadata versionAttribute() {
		return jpaEntityMetadata.versionAttribute();
	}

	@Override
	public JpaAttributeMetadata attribute( final String name ) {
		return jpaEntityMetadata.attribute( name );
	}

	@Override
	public String toString() {
		return jpaEntityMetadata.toString();
	}

	private Repositories emptyRepositories() {
		return new Repositories( new StaticListableBeanFactory() );
	}
}