package org.lightadmin.core.config.domain.scope;

import org.lightadmin.core.config.domain.Builder;
public interface ScopeBuilder extends Builder<Scopes> {

	ScopeBuilder scope( final Scope scope );

	ScopeBuilder scope( String name, final Scope scope );

	ScopeBuilder defaultScope();

	Scopes build();
}