package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.test.model.FilterTestEntity;

@SuppressWarnings( "unused" )
@Administration( FilterTestEntity.class )
public class FilterTestEntityConfiguration {

	public static ScreenContextConfigurationUnit screenContext( final ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
				.screenName( "FilterTest Domain Administration" )
				.menuName( "FilterTest Domain" ).build();
	}

	public static ListViewConfigurationUnit listView( final ListViewConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder
				.field( "textField" ).alias( "Text Field" )
				.field( "integerField" ).alias( "Integer Field" )
				.field( "decimalField" ).alias( "Decimal Field" ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
				.filter( "Text Field", "textField" )
				.filter( "Integer Field", "integerField" )
				.filter( "Decimal Field", "decimalField" ).build();
	}
}