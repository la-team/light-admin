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
package org.lightadmin.api.config.utils;

import java.io.Serializable;

public class EnumElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Object value;
    private final String label;

    private EnumElement(Object value, String label) {
        this.value = value;
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static EnumElement element(int value, String label) {
        return new EnumElement(value, label);
    }

    public static EnumElement element(long value, String label) {
        return new EnumElement(value, label);
    }

    public static EnumElement element(String value, String label) {
        return new EnumElement(value, label);
    }

}
