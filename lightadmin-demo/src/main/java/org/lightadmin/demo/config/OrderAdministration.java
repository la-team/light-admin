package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.view.ScreenContext;
import org.lightadmin.core.view.support.fragment.Fragment;
import org.lightadmin.core.view.support.fragment.FragmentBuilder;
import org.lightadmin.demo.model.Order;

@Administration( Order.class )
public class OrderAdministration {

	public static void configureScreen( ScreenContext screenContext ) {
		screenContext.screenName( "Orders Administration" ).menuName( "Orders" );
	}

	public static Fragment listView( FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "customer.firstname" ).alias( "Customer")
			.field( "billingAddress.city" ).alias("Billing Address")
			.field( "shippingAddress.street" ).alias("Shipping Address")
			.field( "lineItems.0" ).alias("Line Items").build();
	}
}