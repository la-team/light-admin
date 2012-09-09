package org.lightadmin.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention( RetentionPolicy.RUNTIME)
@Documented
public @interface Sidebar {

	String value() default "";

	Location location() default Location.EVERYWHERE;

	public static enum Location {
		SHOW_SCREEN, EDIT_SCREEN, EVERYWHERE
	}

}