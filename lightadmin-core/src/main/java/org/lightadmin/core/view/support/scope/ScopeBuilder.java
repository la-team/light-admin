package org.lightadmin.core.view.support.scope;

import org.lightadmin.core.view.support.Builder;
public interface ScopeBuilder extends Builder<Scopes> {

	ScopeBuilder scope( final Scope scope );

	ScopeBuilder scope( String name, final Scope scope );

	ScopeBuilder defaultScope();

	Scopes build();
}