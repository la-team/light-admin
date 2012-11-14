package org.lightadmin.core.config.domain.field;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;

public class PersistentFieldMetadata extends AbstractFieldMetadata implements DomainTypeAttributeMetadataAware, Persistable {

	private final String field;

	private DomainTypeAttributeMetadata attributeMetadata;

	public PersistentFieldMetadata( final String name, final String field ) {
		super( name );
		this.field = field;
	}

	@Override
	public String getField() {
		return field;
	}

	@Override
	public void setAttributeMetadata( final DomainTypeAttributeMetadata attributeMetadata ) {
		this.attributeMetadata = attributeMetadata;
	}

	@Override
	public DomainTypeAttributeMetadata getAttributeMetadata() {
		return attributeMetadata;
	}

	@Override
	public boolean isSortable() {
		return true;
	}
}