package org.lightadmin.core.view.support;

public interface FilterBuilder extends Builder<Filters> {

	FilterBuilder field( final String fieldName );

	FilterBuilder renderer( final Renderer renderer );

	Filters build();
}