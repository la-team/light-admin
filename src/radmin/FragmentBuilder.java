package radmin;

public interface FragmentBuilder {

	FragmentBuilder field( String fieldName );

	FragmentBuilder alias( String alias );

	FragmentBuilder attribute( String fieldName );

	FragmentBuilder renderer( Renderer renderer );

	Fragment build();
}