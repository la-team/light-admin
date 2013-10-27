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
