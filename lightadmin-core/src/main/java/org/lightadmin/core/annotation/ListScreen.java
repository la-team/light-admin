package org.lightadmin.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention( RetentionPolicy.RUNTIME)
@Documented
public @interface ListScreen {

	String value() default "";

	FragmentType fragmentType() default FragmentType.TABLE;

	enum FragmentType {
		TABLE, GRID, BLOG, BLOCK
	}
}