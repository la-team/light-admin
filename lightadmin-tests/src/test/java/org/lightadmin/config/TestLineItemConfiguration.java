package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.demo.model.LineItem;

//TODO: max: For usage with domain classes from org.lightadmin.demo.model package only
@SuppressWarnings( "unused" )
@Administration( LineItem.class )
public class TestLineItemConfiguration {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Line Items Administration" )
			.menuName( "Line Items" ).build();
	}

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "unitPrice" ).alias( "Unit Price")
			.field("amount").alias("Amount")
			.field("total").alias("Total").build();
	}
}