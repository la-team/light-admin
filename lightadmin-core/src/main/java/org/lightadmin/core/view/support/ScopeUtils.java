package org.lightadmin.core.view.support;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public final class ScopeUtils {

	private ScopeUtils() {
	}

	public static Scope scope( Scope scope ) {
		if ( PredicateScope.class.isAssignableFrom( scope.getClass() ) ) {
			return new PredicateScope( scope );
		}
		return new DefaultScope( scope );
	}

	public static Scope all() {
		return new DefaultScope().name( "All" );
	}

	public static <T> Scope filter( Predicate<T> filter ) {
		return new PredicateScope<T>( filter ).name( filter.getClass().getSimpleName() );
	}

	private static class PredicateScope<T> extends AbstractScope {

		private Predicate<T> predicate = Predicates.alwaysTrue();

		private PredicateScope( final Predicate<T> predicate ) {
			this.predicate = predicate;
		}

		public PredicateScope( Scope scope ) {
			super( scope );
		}

		public Predicate<T> predicate() {
			return predicate;
		}
	}

	private static class DefaultScope extends AbstractScope {

		private DefaultScope() {
		}

		public DefaultScope( Scope scope ) {
			super( scope );
		}
	}

	private static abstract class AbstractScope implements Scope {

		private String name = "Undefined";

		private boolean defaultScope = false;

		protected AbstractScope() {
		}

		public AbstractScope( Scope scope ) {
			this.name = scope.getName();
			this.defaultScope = scope.isDefaultScope();
		}

		@Override
		public Scope name( final String name ) {
			this.name = name;
			return this;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public boolean isDefaultScope() {
			return this.defaultScope;
		}

		@Override
		public Scope defaultScope( final boolean defaultScope ) {
			this.defaultScope = defaultScope;
			return this;
		}
	}
}