package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.util.Transformer;
import org.lightadmin.demo.model.LineItem;
import org.lightadmin.demo.model.Order;
import org.springframework.util.StringUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@SuppressWarnings( "unused" )
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
			.field( "customer.firstname" ).alias( "Customer" )
			.field( "billingAddress.city" ).alias( "Billing Address" )
			.field( "shippingAddress.street" ).alias( "Shipping Address" )
			.field( "lineItems" ).alias( "Order Items" ).renderer( lineItemsFieldValueRenderer() ).build();
	}

	private static Transformer<Order, String> orderNameExtractor() {
		return new Transformer<Order, String>() {
			@Override
			public String apply( final Order order ) {
				return String.format( "Order %s for $%d", order.getCustomer().getFirstname(), order.getTotal().intValue() );
			}
		};
	}

	private static FieldValueRenderer<Order> lineItemsFieldValueRenderer() {
		return new FieldValueRenderer<Order>() {
			@Override
			public String apply( final Order order ) {
				Set<String> lineItems = newLinkedHashSet();
				for ( LineItem lineItem : order.getLineItems() ) {
					lineItems.add( String.format( "Product %s in amount of %d", lineItem.getProduct().getName(), lineItem.getAmount() ) );
				}
				return StringUtils.collectionToDelimitedString( lineItems, ", " );
			}
		};
	}
}