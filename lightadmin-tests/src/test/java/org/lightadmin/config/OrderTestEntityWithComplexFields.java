package org.lightadmin.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.FieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.common.PersistentFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.test.model.TestOrder;
import org.lightadmin.test.renderer.LineItemRenderer;
import org.lightadmin.test.renderer.OrderTotalRenderer;

@SuppressWarnings("unused")
@Administration(TestOrder.class)
public class OrderTestEntityWithComplexFields {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder.screenName( "Administration of Test Order Domain" ).build();
	}

	public static FieldSetConfigurationUnit listView( FieldSetConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder.field( "customer" ).caption( "Customer" ).field( "shippingAddresses" ).caption( "Shipping Addresses" ).renderable( new LineItemRenderer() ).caption( "Line Items" ).renderable( new OrderTotalRenderer() ).caption( "Order Total" ).field( "dueDate" ).caption( "Order Due Date" ).build();
	}

	public static FieldSetConfigurationUnit showView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "customer" ).caption( "Customer" ).field( "shippingAddresses" ).caption( "Shipping Addresses" ).renderable( new LineItemRenderer() ).caption( "Line Items" ).renderable( new OrderTotalRenderer() ).caption( "Order Total" ).field( "dueDate" ).caption( "Order Due Date" ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder.filter( "Id", "id" ).filter( "Customer", "customer" ).filter( "Shipping Addresses", "shippingAddresses" ).filter( "Line Items", "lineItems" ).filter( "Order Due Date", "dueDate" ).build();
	}

	public static FieldSetConfigurationUnit formView( final PersistentFieldSetConfigurationUnitBuilder formViewBuilder ) {
		return formViewBuilder.field( "customer" ).caption( "Customer" ).field( "shippingAddresses" ).caption( "Shipping Addresses" ).field( "lineItems" ).caption( "Line Items" ).field( "dueDate" ).caption( "Order Due Date" ).build();
	}
}
