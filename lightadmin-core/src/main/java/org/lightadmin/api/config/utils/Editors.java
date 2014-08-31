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

import org.lightadmin.core.view.editor.EnumFieldEditControl;
import org.lightadmin.core.view.editor.JspFragmentFieldControl;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.lightadmin.api.config.utils.EnumElement.element;

@SuppressWarnings("unused")
public abstract class Editors {

    public static JspFragmentFieldControl textArea() {
        return new JspFragmentFieldControl("/views/editors/textarea-field-edit-control.jsp");
    }

    public static JspFragmentFieldControl wysiwyg() {
        return new JspFragmentFieldControl("/views/editors/wysiwyg-field-edit-control.jsp");
    }

    public static JspFragmentFieldControl enumeration(List<EnumElement> elements) {
        return new EnumFieldEditControl(elements);
    }

    public static JspFragmentFieldControl enumeration(EnumElement... elements) {
        return enumeration(asList(elements));
    }

    public static JspFragmentFieldControl enumeration(String... values) {
        final List<EnumElement> elements = newArrayList();
        for (String value : values) {
            elements.add(element(value, value));
        }
        return enumeration(elements);
    }
}