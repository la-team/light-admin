package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.test.model.TestOrder;

@SuppressWarnings( "unused" )
@Administration( TestOrder.class )
public class OrderTestEntityWithDefaultId {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
				.screenName( "Administration of Test Order Domain" )
				.menuName( "Test Order Domain" ).build();
	}

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder
				.field( "name" ).alias( "Name" ).build();
	}

//  TODO: max: please uncomment this when LA-20 is implemented ;)
//	public static QuickViewConfigurationUnit quickView( QuickViewConfigurationUnitBuilder quickViewBuilder ) {
//		return quickViewBuilder
//				.field( "name" ).alias( "Order Total" ).build();
//	}

}
