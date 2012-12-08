package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnit;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnitBuilder;
import org.lightadmin.test.model.TestOrder;

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

	public static ShowViewConfigurationUnit showView( final ShowViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "id" ).alias( "Order Id" )
			.field( "name" ).alias( "Name" ).build();
	}
}