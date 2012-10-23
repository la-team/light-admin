package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.support.Builder;

public interface FragmentBuilder extends Builder<Fragment> {

	FragmentBuilder field( String fieldName );

	FragmentBuilder alias( String alias );

	FragmentBuilder attribute( String fieldName );

	FragmentBuilder renderer( FieldValueRenderer<?> renderer );

	Fragment build();
}