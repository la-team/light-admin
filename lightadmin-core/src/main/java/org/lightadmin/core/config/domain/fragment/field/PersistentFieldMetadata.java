package org.lightadmin.core.config.domain.fragment.field;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;

public class PersistentFieldMetadata extends AbstractFieldMetadata implements DomainTypeAttributeMetadataAware {

	private final String field;

	private DomainTypeAttributeMetadata attributeMetadata;

	protected PersistentFieldMetadata( final String name, final String field ) {
		super( name );
		this.field = field;
	}

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
		return false;
	}
}