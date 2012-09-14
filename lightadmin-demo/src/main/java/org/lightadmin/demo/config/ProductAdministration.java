package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.view.ScreenContext;
import org.lightadmin.core.view.support.Fragment;
import org.lightadmin.core.view.support.FragmentBuilder;
import org.lightadmin.demo.model.Product;

@Administration( Product.class )
public class ProductAdministration {

	public static void configureScreen( ScreenContext screenContext ) {
		screenContext.screenName( "Products Administration" ).menuName( "Products" );
	}

	public static Fragment listView( FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "name" ).alias( "Name")
			.field( "description" ).alias("Description")
			.field( "price" ).alias("Price").build();
	}
}