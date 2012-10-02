package org.lightadmin.demo.config;

import com.google.common.base.Predicates;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.view.ScreenContext;
import org.lightadmin.core.view.support.filter.FilterBuilder;
import org.lightadmin.core.view.support.filter.Filters;
import org.lightadmin.core.view.support.fragment.Fragment;
import org.lightadmin.core.view.support.fragment.FragmentBuilder;
import org.lightadmin.core.view.support.scope.ScopeBuilder;
import org.lightadmin.core.view.support.scope.Scopes;
import org.lightadmin.demo.model.Customer;

import static org.lightadmin.core.view.support.renderer.Renderers.select;
import static org.lightadmin.core.view.support.scope.ScopeUtils.all;
import static org.lightadmin.core.view.support.scope.ScopeUtils.filter;

@Administration( Customer.class )
public class CustomerAdministration {

	public static void configureScreen( ScreenContext screenContext ) {
		screenContext.screenName( "Customers Administration" ).menuName( "Customers" );
	}

	public static Fragment listView( final FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "firstname" ).alias( "First Name")
			.field( "lastname" ).alias("Last Name")
			.field( "emailAddress" ).alias( "Email Address" ).build();
	}

	public static Scopes scopes( final ScopeBuilder scopeBuilder ) {
		return scopeBuilder
			.scope( "All", all() ).defaultScope()
			.scope( "Buyers", filter( Predicates.alwaysTrue() ) )
			.scope( "Sellers", filter( Predicates.alwaysTrue() ) ).build();
	}

	public static Filters filters( final FilterBuilder filterBuilder ) {
		return filterBuilder
			.field( "firstname" ).renderer( select( new String[] { "Yes", "No" } ))
			.field( "addresses" ).build();
	}
}