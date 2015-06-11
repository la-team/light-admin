/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.api.config.annotation;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RUNTIME)
public @interface FileReference {

    String baseDirectory() default "";

    /**
     * this inner annotaion can be used in both 
     * @FileReference and @Lob annotated fields
     * 
     */
    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface Constraints {
        /**
         * allowed extensions (comma separated or *)
         *
         * @return comma separated extensions
         */
        String value() default "jpg,jpeg,png";
        
        /**
         * file size limit in megabytes
         *
         * @return file size limit in megabytes
         */
        int limit() default 10;
    }
}
