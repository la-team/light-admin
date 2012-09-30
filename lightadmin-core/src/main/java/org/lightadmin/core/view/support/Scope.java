package org.lightadmin.core.view.support;

public interface Scope {

	Scope name( String name );

	String getName();

	boolean isDefaultScope();

	Scope defaultScope( boolean isDefault );
}