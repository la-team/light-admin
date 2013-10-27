package org.lightadmin.api.config.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@java.lang.annotation.Target(TYPE)
@java.lang.annotation.Retention(RUNTIME)
public @interface Administration {

    Class<?> value();
}