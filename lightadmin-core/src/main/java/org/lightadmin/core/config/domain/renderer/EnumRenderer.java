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
package org.lightadmin.core.config.domain.renderer;

import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.springframework.util.ObjectUtils;

public class EnumRenderer<F> implements FieldValueRenderer<F> {

    private final EnumElement[] elements;

    public EnumRenderer(EnumElement... elements) {
        this.elements = elements;
    }

    @Override
    public String apply(F input) {
        for (EnumElement elem : elements) {
            if (ObjectUtils.nullSafeEquals(elem.getValue(), input)) {
                return elem.getLabel();
            }
        }
        return (input != null) ? input.toString() : "";
    }

}
