package org.lightadmin.field;

public interface BaseSelect {

	public abstract void select( String... optionLabels );

	public abstract void clear();

	void searchAndSelect( String searchString, String labelToSelect );
}
