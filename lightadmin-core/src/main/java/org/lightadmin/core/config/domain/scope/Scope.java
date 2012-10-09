package org.lightadmin.core.config.domain.scope;

public interface Scope {

	Scope name( String name );

	String getName();

	boolean isDefaultScope();

	Scope defaultScope( boolean isDefault );
}