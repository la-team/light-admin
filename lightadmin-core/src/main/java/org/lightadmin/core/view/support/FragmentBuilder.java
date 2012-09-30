package org.lightadmin.core.view.support;

public interface FragmentBuilder extends Builder<Fragment> {

	FragmentBuilder field( String fieldName );

	FragmentBuilder alias( String alias );

	FragmentBuilder attribute( String fieldName );

	FragmentBuilder renderer( Renderer renderer );

	Fragment build();
}