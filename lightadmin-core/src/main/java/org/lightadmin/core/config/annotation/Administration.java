package org.lightadmin.core.config.annotation;

@java.lang.annotation.Target( {java.lang.annotation.ElementType.TYPE} )
@java.lang.annotation.Retention( java.lang.annotation.RetentionPolicy.RUNTIME )
@java.lang.annotation.Documented
public @interface Administration {

	Class<?> value();
}