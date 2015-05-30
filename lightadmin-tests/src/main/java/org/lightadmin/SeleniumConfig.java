package org.lightadmin;

import org.lightadmin.api.config.management.rmi.DataManipulationService;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
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
@ComponentScan("org.lightadmin.page")
@PropertySource(value = "classpath:selenium.properties")
public class SeleniumConfig {

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(destroyMethod = "destroy")
    public SeleniumContext seleniumContext() {
        return new SeleniumContext(webDriver(), baseUrl(), webDriverWaitTimeout());
    }

    @Bean
    public GlobalConfigurationManagementService globalConfigurationManagementService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://127.0.0.1:1199/GlobalConfigurationManagementService");
        rmiProxyFactoryBean.setServiceInterface(GlobalConfigurationManagementService.class);
        rmiProxyFactoryBean.afterPropertiesSet();
        return (GlobalConfigurationManagementService) rmiProxyFactoryBean.getObject();
    }

    @Bean
    public DataManipulationService dataManipulationService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://127.0.0.1:1199/DataManipulationService");
        rmiProxyFactoryBean.setServiceInterface(DataManipulationService.class);
        rmiProxyFactoryBean.afterPropertiesSet();
        return (DataManipulationService) rmiProxyFactoryBean.getObject();
    }

    @Bean
    public LoginService loginService() {
        return new LoginService();
    }

    private boolean isSecurityEnabled() {
        return Boolean.valueOf(environment.getProperty("security.enabled"));
    }

    private URL baseUrl() {
        try {
            return new URL(environment.getProperty("baseUrl"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Base URL wring format. Please check selenium.properties file.");
        }
    }

    public RemoteWebDriver webDriver() {
        final RemoteWebDriver driver = webDriver(environment.getProperty("selenium.browser"));

        driver.manage().timeouts().implicitlyWait(webDriverWaitTimeout(), TimeUnit.SECONDS);

        driver.manage().window().maximize();

        return driver;
    }

    private long webDriverWaitTimeout() {
        return environment.getProperty("element.wait.sec", Long.class);
    }

    private RemoteWebDriver webDriver(final String seleniumBrowser) {
        if ("chrome".equals(seleniumBrowser)) {
            return new ChromeDriver();
        }

        if ("safari".equals(seleniumBrowser)) {
            return new SafariDriver();
        }

        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("focusmanager.testmode", true);
        return new FirefoxDriver(profile);
    }
}