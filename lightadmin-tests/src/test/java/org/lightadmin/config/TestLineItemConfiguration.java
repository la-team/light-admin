package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.demo.model.LineItem;

//TODO: max: For usage with domain classes from org.lightadmin.demo.model package only
@SuppressWarnings( "unused" )
@Administration( LineItem.class )
public class TestLineItemConfiguration {

	public static ScreenContext screenContext( ScreenContextBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Line Items Administration" )
			.menuName( "Line Items" ).build();
	}

	public static Fragment listView( FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field("price").alias( "Price")
			.field("amount").alias("Amount").build();
	}
}