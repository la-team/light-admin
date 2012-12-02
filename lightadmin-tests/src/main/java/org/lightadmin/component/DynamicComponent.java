package org.lightadmin.component;

import org.lightadmin.SeleniumContext;

public abstract class DynamicComponent<T extends DynamicComponent> extends BaseComponent {

	protected DynamicComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public T get() {
		try {
			isLoaded();
			return (T) this;
		} catch (Error e ) {
			load();
		}

		isLoaded();

		return (T) this;
	}

	protected abstract void isLoaded() throws Error;

	protected abstract void load();
}
