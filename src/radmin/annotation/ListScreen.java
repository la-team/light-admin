package radmin.annotation;

public @interface ListScreen {

	String value() default "";

	FragmentType fragmentType() default FragmentType.TABLE;

	enum FragmentType {
		TABLE, GRID, BLOG, BLOCK
	}
}
