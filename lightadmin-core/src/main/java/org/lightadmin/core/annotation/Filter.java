package org.lightadmin.core.annotation;

import java.lang.annotation.*;

@Target( {ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Filter {

}
