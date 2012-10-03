package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;

public class JpaDomainTypeAttributeMetadata implements DomainTypeAttributeMetadata {

	private JpaAttributeMetadata attributeMetadata;

	public JpaDomainTypeAttributeMetadata( JpaAttributeMetadata attributeMetadata ) {
		this.attributeMetadata = attributeMetadata;
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
		return attributeMetadata.isCollectionLike();
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
	public Object getValue( final Object target ) {
		return attributeMetadata.get( target );
	}
}