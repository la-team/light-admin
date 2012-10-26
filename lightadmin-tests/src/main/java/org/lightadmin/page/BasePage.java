package org.lightadmin.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.net.URL;

public abstract class BasePage<P extends LoadableComponent<P>> extends LoadableComponent<P> {

	private LoadableComponent<?> parent;

	protected final WebDriver webDriver;

	protected final URL baseUrl;

	protected BasePage( final WebDriver webDriver, final URL baseUrl ) {
		this.webDriver = webDriver;
		this.baseUrl = baseUrl;

		PageFactory.initElements( this.webDriver, this );
	}

	public BasePage( WebDriver webDriver, URL baseUrl, LoadableComponent<?> parent ) {
		this( webDriver, baseUrl );
		this.parent = parent;
	}

	protected abstract void loadPage();

	@Override
	protected void load() {
		if ( parent != null ) {
			parent.get();
		}
		loadPage();
	}

	protected void clearAndType( WebElement field, String text ) {
		field.clear();
		field.sendKeys( text );
	}
}