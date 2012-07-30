package radmin;

import java.util.List;

public interface ScopeBuilder {

	ScopeBuilder scope( String name, final Scopes.Scope all );

	ScopeBuilder defaultScope();

	List<Scopes.Scope> build();
}