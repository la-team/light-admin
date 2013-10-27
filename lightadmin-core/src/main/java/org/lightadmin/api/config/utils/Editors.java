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