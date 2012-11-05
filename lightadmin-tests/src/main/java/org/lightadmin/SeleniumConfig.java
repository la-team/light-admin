package org.lightadmin;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceDTO;
import org.lightadmin.core.config.rmi.GlobalConfigurationManagementService;
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

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan( "org.lightadmin.page" )
@PropertySource( value = "classpath:selenium.properties" )
public class SeleniumConfig {

	@Autowired
	private Environment environment;

	@PostConstruct
	public void init() {
		final DomainConfigurationSourceDTO domainConfigurationSourceDTO = new DomainConfigurationSourceDTO( TestAdministrationConfiguration.class );
		globalConfigurationManagementService().registerDomainTypeConfiguration( domainConfigurationSourceDTO );
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public URL baseUrl() throws MalformedURLException {
		return new URL( environment.getProperty( "baseUrl" ) );
	}

	@Bean( destroyMethod = "quit" )
	public WebDriver webDriver() {
		final WebDriver driver = webDriver( environment.getProperty( "selenium.browser" ) );

		driver.manage().timeouts().implicitlyWait( environment.getProperty( "element.wait.sec", Long.class ), TimeUnit.SECONDS );

		driver.manage().window().maximize();

		return driver;
	}

	@Bean
	public GlobalConfigurationManagementService globalConfigurationManagementService() {
		RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
		rmiProxyFactoryBean.setServiceUrl( "rmi://localhost:1199/GlobalConfigurationManagementService" );
		rmiProxyFactoryBean.setServiceInterface( GlobalConfigurationManagementService.class );
		rmiProxyFactoryBean.afterPropertiesSet();
		return ( GlobalConfigurationManagementService ) rmiProxyFactoryBean.getObject();
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