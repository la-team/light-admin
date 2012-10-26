package org.lightadmin;

import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.URL;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( loader = AnnotationConfigContextLoader.class, classes = SeleniumConfig.class )
public class SeleniumIntegrationTest {

	@Autowired
	private WebDriver webDriver;

	@Autowired
	private URL baseUrl;

	protected WebDriver webDriver() {
		return webDriver;
	}

	protected URL baseUrl() {
		return baseUrl;
	}
}