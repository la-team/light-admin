package org.lightadmin.core.config.domain.scope;

import java.io.Serializable;

public interface ScopeMetadata extends Serializable {

	ScopeMetadata name( String name );

	String getName();

	boolean isDefaultScope();

	ScopeMetadata defaultScope( boolean isDefault );
}