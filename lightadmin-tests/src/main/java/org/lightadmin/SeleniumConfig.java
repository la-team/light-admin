package org.lightadmin;

import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan( "org.lightadmin.page" )
@PropertySource( value = "classpath:selenium.properties" )
public class SeleniumConfig {

	@Autowired
	private Environment environment;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean( destroyMethod = "destroy" )
	public SeleniumContext seleniumContext() {
		return new SeleniumContext( webDriver(), baseUrl(), webDriverWaitTimeout() );
	}

	@Bean
	public GlobalConfigurationManagementService globalConfigurationManagementService() {
		RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
		rmiProxyFactoryBean.setServiceUrl( "rmi://localhost:1199/GlobalConfigurationManagementService" );
		rmiProxyFactoryBean.setServiceInterface( GlobalConfigurationManagementService.class );
		rmiProxyFactoryBean.afterPropertiesSet();
		return ( GlobalConfigurationManagementService ) rmiProxyFactoryBean.getObject();
	}

	private URL baseUrl() {
		try {
			return new URL( environment.getProperty( "baseUrl" ) );
		} catch ( MalformedURLException e ) {
			throw new RuntimeException( "Base URL wring format. Please check selenium.properties file." );
		}
	}

	public WebDriver webDriver() {
		final WebDriver driver = webDriver( environment.getProperty( "selenium.browser" ) );

		driver.manage().timeouts().implicitlyWait( webDriverWaitTimeout(), TimeUnit.SECONDS );

		driver.manage().window().maximize();

		return driver;
	}

	private long webDriverWaitTimeout() {
		return environment.getProperty( "element.wait.sec", Long.class );
	}

	private WebDriver webDriver( final String seleniumBrowser ) {
		if ( "chrome".equals( seleniumBrowser ) ) {
			return new ChromeDriver();
		}

		if ( "safari".equals( seleniumBrowser ) ) {
			return new SafariDriver();
		}

		return new FirefoxDriver();
	}
}