package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnit;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnitBuilder;
import org.lightadmin.demo.model.LineItem;
import org.lightadmin.demo.model.Order;
import org.springframework.util.StringUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@SuppressWarnings( "unused" )
@Administration( Order.class )
public class OrderAdministration {

	public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
		return configurationBuilder.nameExtractor( orderNameExtractor() ).build();
	}

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Orders Administration" )
			.menuName( "Orders" ).build();
	}

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "customer" ).alias( "Customer" )
			.field( "billingAddress" ).alias( "Billing Address" )
			.field( "shippingAddress" ).alias( "Shipping Address" )
			.field( "lineItems" ).alias( "Order Items" ).renderer( lineItemsFieldValueRenderer() ).build();
	}

	public static ShowViewConfigurationUnit showView( final ShowViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "customer" ).alias( "Customer" )
			.field( "billingAddress" ).alias( "Billing Address" )
			.field( "shippingAddress" ).alias( "Shipping Address" )
			.field( "lineItems" ).alias( "Order Items" ).renderer( lineItemsFieldValueRenderer() ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.filter( "Customer", "customer" )
			.filter( "Billing Address", "billingAddress" )
			.filter( "Shipping Address", "shippingAddress" ).build();
	}

	private static EntityNameExtractor<Order> orderNameExtractor() {
		return new EntityNameExtractor<Order>() {
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