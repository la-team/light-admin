package org.lightadmin.core.view.support.fragment;

import org.lightadmin.core.view.support.Builder;
import org.lightadmin.core.view.support.renderer.Renderer;
public interface FragmentBuilder extends Builder<Fragment> {

	FragmentBuilder field( String fieldName );

	FragmentBuilder alias( String alias );

	FragmentBuilder attribute( String fieldName );

	FragmentBuilder renderer( Renderer renderer );

	Fragment build();
}