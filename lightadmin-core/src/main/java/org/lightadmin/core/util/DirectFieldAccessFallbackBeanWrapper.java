package org.lightadmin.core.util;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NotWritablePropertyException;

import java.lang.reflect.Field;

import static org.springframework.util.ReflectionUtils.*;

public class DirectFieldAccessFallbackBeanWrapper extends BeanWrapperImpl {

    public DirectFieldAccessFallbackBeanWrapper(Object entity) {
        super(entity);
    }

    @Override
    public Object getPropertyValue(String propertyName) {
        try {
            return super.getPropertyValue(propertyName);
        } catch (NotReadablePropertyException e) {
            Field field = findField(getWrappedClass(), propertyName);
            makeAccessible(field);
            return getField(field, getWrappedInstance());
        }
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) {
        try {
            super.setPropertyValue(propertyName, value);
        } catch (NotWritablePropertyException e) {
            Field field = findField(getWrappedClass(), propertyName);
            makeAccessible(field);
            setField(field, getWrappedInstance(), value);
        }
    }
}