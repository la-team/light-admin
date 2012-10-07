package org.lightadmin.demo.config;

import com.google.common.base.Function;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.view.support.configuration.EntityConfiguration;
import org.lightadmin.core.view.support.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.view.support.context.ScreenContext;
import org.lightadmin.core.view.support.context.ScreenContextBuilder;
import org.lightadmin.core.view.support.fragment.Fragment;
import org.lightadmin.core.view.support.fragment.FragmentBuilder;
import org.lightadmin.demo.model.Order;

@Administration( Order.class )
public class OrderAdministration {

	public static EntityConfiguration configuration( EntityConfigurationBuilder configurationBuilder ) {
		return configurationBuilder.nameExtractor( orderNameExtractor() ).build();
	}

	public static ScreenContext screenContext( ScreenContextBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Orders Administration" )
			.menuName( "Orders" ).build();
	}

	public static Fragment listView( FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "customer.firstname" ).alias( "Customer")
			.field( "billingAddress.city" ).alias("Billing Address")
			.field( "shippingAddress.street" ).alias("Shipping Address")
			.field( "lineItems.0" ).alias("Line Items").build();
	}

	private static Function<?, String> orderNameExtractor() {
		return new Function<Order, String>() {
			@Override
			public String apply( final Order order ) {
				return String.format( "Order %s for $%d", order.getCustomer().getFirstname(), order.getTotal().intValue() );
			}
		};
	}
}