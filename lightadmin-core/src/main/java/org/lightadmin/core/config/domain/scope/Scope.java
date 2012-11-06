package org.lightadmin.core.config.domain.scope;

import java.io.Serializable;

public interface Scope extends Serializable  {

	Scope name( String name );

	String getName();

	boolean isDefaultScope();

	Scope defaultScope( boolean isDefault );
}