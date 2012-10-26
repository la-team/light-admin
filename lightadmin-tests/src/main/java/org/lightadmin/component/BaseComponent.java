package org.lightadmin.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.net.URL;

public abstract class BaseComponent {

	protected final WebDriver webDriver;
	protected final URL baseUrl;

	protected BaseComponent( final WebDriver webDriver, final URL baseUrl ) {
		this.webDriver = webDriver;
		this.baseUrl = baseUrl;

		PageFactory.initElements( webDriver, this );
	}
}