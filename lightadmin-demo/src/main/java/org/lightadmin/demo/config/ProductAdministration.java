package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.view.support.configuration.EntityConfiguration;
import org.lightadmin.core.view.support.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.view.support.context.ScreenContext;
import org.lightadmin.core.view.support.context.ScreenContextBuilder;
import org.lightadmin.core.view.support.fragment.Fragment;
import org.lightadmin.core.view.support.fragment.FragmentBuilder;
import org.lightadmin.demo.model.Product;

@Administration( Product.class )
public class ProductAdministration {

	public static EntityConfiguration configuration( EntityConfigurationBuilder configurationBuilder ) {
		return configurationBuilder.nameField( "name" ).build();
	}

	public static ScreenContext screenContext( ScreenContextBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Products Administration" )
			.menuName( "Products" ).build();
	}

	public static Fragment listView( FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "name" ).alias( "Name")
			.field( "description" ).alias("Description")
			.field( "price" ).alias("Price").build();
	}
}