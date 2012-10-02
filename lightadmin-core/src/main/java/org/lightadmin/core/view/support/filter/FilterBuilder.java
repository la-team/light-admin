package org.lightadmin.core.view.support.filter;

import org.lightadmin.core.view.support.Builder;
import org.lightadmin.core.view.support.renderer.Renderer;
public interface FilterBuilder extends Builder<Filters> {

	FilterBuilder field( final String fieldName );

	FilterBuilder renderer( final Renderer renderer );

	Filters build();
}