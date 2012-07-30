package radmin;

import com.google.common.base.Predicate;

public class Scopes {

	public static Scope all() {
		return null;
	}

	public static Scope filter( Predicate filter ) {
		return null;
	}

	public static interface Scope {
		Scope defaultScope();
		Scope defaultScope( boolean isDefaultScope );
	}
}