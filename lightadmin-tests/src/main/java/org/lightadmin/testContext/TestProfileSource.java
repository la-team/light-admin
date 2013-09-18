package org.lightadmin.testContext;

import org.springframework.test.annotation.ProfileValueSource;

import java.io.IOException;
import java.util.Properties;

public class TestProfileSource implements ProfileValueSource {

	@Override
	public String get( String key ) {
		Properties properties = new Properties();
		try {
			properties.load( getClass().getClassLoader().getResourceAsStream( "selenium.properties" ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return properties.getProperty( key );
	}

}
