package org.lightadmin.core.view.editor;

import org.lightadmin.core.config.domain.common.EnumElement;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.lightadmin.core.config.domain.common.EnumElement.element;

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
        List<EnumElement> elements = new ArrayList<EnumElement>(values.length);
        for (String value : values) {
            elements.add(element(value, value));
        }
        return enumeration(elements);
    }
}