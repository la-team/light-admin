package util;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Configuration {

    private static Properties propertiesReader;

    private static int elementWaitTime;
    private static String baseUrl;

    static {

        loadPropertyFile();

        baseUrl = propertiesReader.getProperty( "baseUrl" );
        elementWaitTime = Integer.parseInt( propertiesReader.getProperty("element.wait.sec") );
    }

    private static void loadPropertyFile() {

        propertiesReader = new Properties();
        URL propertiesUrl = ClassLoader.getSystemResource("test.properties");

        try {
            propertiesReader.load( propertiesUrl.openStream() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static int getElementWaitTime() {
        return elementWaitTime;
    }
}
