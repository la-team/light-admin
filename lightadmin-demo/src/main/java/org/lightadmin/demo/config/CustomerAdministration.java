package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.view.ScreenContext;
import org.lightadmin.core.view.support.Fragment;
import org.lightadmin.core.view.support.FragmentBuilder;
import org.lightadmin.demo.model.Customer;

@Administration( Customer.class )
public class CustomerAdministration {

	public static void configureScreen( ScreenContext screenContext ) {
		screenContext.screenName( "Customers Administration" ).menuName( "Customers" );
	}

	public static Fragment listView( FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "firstname" ).alias( "First Name")
			.field("lastname").alias("Last Name")
			.field( "emailAddress" ).alias( "Email Address" ).build();
	}
}