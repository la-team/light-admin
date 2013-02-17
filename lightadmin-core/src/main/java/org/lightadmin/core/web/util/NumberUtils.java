package org.lightadmin.core.web.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class NumberUtils {

	public static boolean isNumber( String str ) {
		return org.apache.commons.lang.math.NumberUtils.isNumber( str );
	}

	public static Number parseNumber( String text, Class<? extends Number> targetType ) {
		final Number number = org.apache.commons.lang.math.NumberUtils.createNumber( text );

		try {
			return org.springframework.util.NumberUtils.convertNumberToTargetClass( number, targetType );
		} catch ( IllegalArgumentException ex ) {
			return number;
		}
	}

	public static boolean isNumberInteger( Class type ) {
		if ( byte.class.equals( type ) || Byte.class.equals( type ) ) {
			return true;
		}
		if ( short.class.equals( type ) || Short.class.equals( type ) ) {
			return true;
		}
		if ( int.class.equals( type ) || Integer.class.equals( type ) ) {
			return true;
		}
		if ( long.class.equals( type ) || Long.class.equals( type ) ) {
			return true;
		}
		return BigInteger.class.equals( type );
	}

	public static boolean isNumberFloat( Class type ) {
		if ( float.class.equals( type ) || Float.class.equals( type ) ) {
			return true;
		}
		if ( double.class.equals( type ) || Double.class.equals( type ) ) {
			return true;
		}
		if ( BigDecimal.class.equals( type ) || Number.class.equals( type ) ) {
			return true;
		}
		return false;
	}
}