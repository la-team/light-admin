package org.lightadmin.core.config.domain.field;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;

import javax.persistence.GeneratedValue;

public class PersistentFieldMetadata extends AbstractFieldMetadata implements DomainTypeAttributeMetadataAware, Persistable {

	private final String field;

	private boolean primaryKey;

	private DomainTypeAttributeMetadata attributeMetadata;

	public PersistentFieldMetadata( final String name, final String field, boolean primaryKey ) {
		this( name, field );
		this.primaryKey = primaryKey;
	}

	public PersistentFieldMetadata( final String name, final String field ) {
		super( name );
		this.field = field;
	}

	@Override
	public String getField() {
		return field;
	}

	@Override
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	@Override
	public boolean isGeneratedValue() {
		return attributeMetadata.hasAnnotation( GeneratedValue.class );
	}

	public void setPrimaryKey( final boolean primaryKey ) {
		this.primaryKey = primaryKey;
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

	@Override
	public String getUuid() {
		return attributeMetadata.getName();
	}
}