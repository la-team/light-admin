package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.test.model.TestOrder;
import org.lightadmin.test.renderer.LineItemRenderer;

@SuppressWarnings( "unused" )
@Administration( TestOrder.class )
public class SimpleOrderTestEntityConfiguration {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
				.screenName( "Administration of Test Order Domain" )
				.menuName( "Test Order Domain" ).build();
	}

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder
				.field( "id" ).alias( "Order Id" )
				.field( "name" ).alias( "Name" ).build();
	}
}
