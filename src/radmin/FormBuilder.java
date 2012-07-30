package radmin;

public interface FormBuilder {

	FormBuilder field( String fieldName );

	Fragment fragment();

	FormBuilder renderer( Renderer renderer );
}