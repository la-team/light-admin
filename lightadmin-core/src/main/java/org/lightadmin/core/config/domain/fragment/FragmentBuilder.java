package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.renderer.Renderer;
import org.lightadmin.core.config.domain.support.Builder;
public interface FragmentBuilder extends Builder<Fragment> {

	FragmentBuilder field( String fieldName );

	FragmentBuilder alias( String alias );

	FragmentBuilder attribute( String fieldName );

	FragmentBuilder renderer( Renderer renderer );

	Fragment build();
}