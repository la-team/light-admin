package org.lightadmin.core.config.domain.field;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractFieldMetadata implements FieldMetadata, Serializable {

	private String name;

	private final int order;

	private final UUID uuid;

	protected AbstractFieldMetadata( final String name ) {
		this( name, 0 );
	}

	protected AbstractFieldMetadata( final String name, int order ) {
		this.name = name;
		this.order = order;
		this.uuid = UUID.randomUUID();
	}

	public void setName( String name ) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSortOrder() {
		return order;
	}

	@Override
	public String getUuid() {
		return uuid.toString();
	}

	@Override
	public boolean isSortable() {
		return false;
	}

	@Override
	public boolean equals( final Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final AbstractFieldMetadata that = ( AbstractFieldMetadata ) o;

		return uuid.equals( that.uuid );
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}