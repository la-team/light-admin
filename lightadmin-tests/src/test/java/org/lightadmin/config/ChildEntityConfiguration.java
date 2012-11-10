package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.demo.model.ChildTestEntity;

@SuppressWarnings( "unused" )
@Administration( ChildTestEntity.class )
public class ChildEntityConfiguration {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
				.screenName( "Test Child Domain Administration" )
				.menuName( "Test Child Domain" ).build();
	}

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder
				.field( "name" ).alias( "Name" )
				.field( "parent_id" ).alias( "Parent_ID" )
				.field( "parent.name" ).alias( "Parent Name" ).build();
	}
}
