package radmin.annotation;

public @interface Sidebar {

	String value() default "";

	Location location() default Location.EVERYWHERE;

	public static enum Location {
		SHOW_SCREEN, EDIT_SCREEN, EVERYWHERE
	}

}
