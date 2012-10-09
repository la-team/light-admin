package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.demo.model.Product;

@SuppressWarnings( "unused" )
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