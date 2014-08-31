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
package org.lightadmin.core.util;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class NumberUtils {

    public static boolean isNumber(String str) {
        return org.apache.commons.lang3.math.NumberUtils.isNumber(str);
    }

    public static Number parseNumber(String text, Class<? extends Number> targetType) {
        final Number number = org.apache.commons.lang3.math.NumberUtils.createNumber(text);

        try {
            return convertNumberToTargetClass(number, targetType);
        } catch (IllegalArgumentException ex) {
            return number;
        }
    }

    public static boolean isNumberInteger(Class type) {
        if (byte.class.equals(type) || Byte.class.equals(type)) {
            return true;
        }
        if (short.class.equals(type) || Short.class.equals(type)) {
            return true;
        }
        if (int.class.equals(type) || Integer.class.equals(type)) {
            return true;
        }
        if (long.class.equals(type) || Long.class.equals(type)) {
            return true;
        }
        return BigInteger.class.equals(type);
    }

    public static boolean isNumberFloat(Class type) {
        if (float.class.equals(type) || Float.class.equals(type)) {
            return true;
        }
        if (double.class.equals(type) || Double.class.equals(type)) {
            return true;
        }
        if (BigDecimal.class.equals(type) || Number.class.equals(type)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) throws IllegalArgumentException {
        Assert.notNull(number, "Number must not be null");
        Assert.notNull(targetClass, "Target class must not be null");

        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (targetClass.equals(byte.class)) {
            long value = number.longValue();
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Byte(number.byteValue());
        } else if (targetClass.equals(short.class)) {
            long value = number.longValue();
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Short(number.shortValue());
        } else if (targetClass.equals(int.class)) {
            long value = number.longValue();
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) new Integer(number.intValue());
        } else if (targetClass.equals(long.class)) {
            return (T) new Long(number.longValue());
        } else if (targetClass.equals(float.class)) {
            return (T) Float.valueOf(number.toString());
        } else if (targetClass.equals(double.class)) {
            return (T) Double.valueOf(number.toString());
        } else {
            return org.springframework.util.NumberUtils.convertNumberToTargetClass(number, targetClass);
        }
    }

    private static void raiseOverflowException(Number number, Class targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }
}