package org.lightadmin.core.config.domain.fragment.field;

import java.util.UUID;

abstract class AbstractFieldMetadata implements FieldMetadata {

	private final String name;

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

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}

	public UUID getUuid() {
		return uuid;
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

		return uuid.equals( that.getUuid() );
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}