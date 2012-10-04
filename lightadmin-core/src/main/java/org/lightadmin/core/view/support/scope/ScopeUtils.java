package org.lightadmin.core.view.support.scope;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.data.jpa.domain.Specification;

public final class ScopeUtils {

	private ScopeUtils() {
	}

	@SuppressWarnings( "unchecked" )
	public static Scope scope( Scope scope ) {
		if ( PredicateScope.class.isAssignableFrom( scope.getClass() ) ) {
			return new PredicateScope( ( PredicateScope ) scope );
		}

		if ( SpecificationScope.class.isAssignableFrom( scope.getClass() ) ) {
			return new SpecificationScope( ( SpecificationScope ) scope );
		}

		return new DefaultScope( scope );
	}

	public static Scope all() {
		return new DefaultScope().name( "All" );
	}

	public static <T> Scope filter( Predicate<T> filter ) {
		return new PredicateScope<T>( filter ).name( filter.getClass().getSimpleName() );
	}

	public static <T> Scope specification( Specification<T> specification ) {
		return new SpecificationScope<T>( specification ).name( specification.getClass().getSimpleName() );
	}

	public static class SpecificationScope<T> extends AbstractScope {

		private final Specification<T> specification;

		private SpecificationScope( final Specification<T> specification ) {
			this.specification = specification;
		}

		private SpecificationScope( final SpecificationScope<T> scope ) {
			super( scope );
			this.specification = scope.specification();
		}

		public Specification<T> specification() {
			return specification;
		}
	}

	public static class PredicateScope<T> extends AbstractScope {

		private Predicate<T> predicate = Predicates.alwaysTrue();

		private PredicateScope( final Predicate<T> predicate ) {
			this.predicate = predicate;
		}

		public PredicateScope( PredicateScope<T> scope ) {
			super( scope );
			this.predicate = scope.predicate();
		}

		public Predicate<T> predicate() {
			return predicate;
		}
	}

	public static class DefaultScope extends AbstractScope {

		private DefaultScope() {
		}

		public DefaultScope( Scope scope ) {
			super( scope );
		}
	}

	public static abstract class AbstractScope implements Scope {

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