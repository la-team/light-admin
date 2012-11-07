package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.demo.model.Product;

@SuppressWarnings( "unused" )
@Administration( Product.class )
public class ProductAdministration {

	public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
		return configurationBuilder.nameField( "name" ).build();
	}

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Products Administration" )
			.menuName( "Products" ).build();
	}

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "name" ).alias( "Name")
			.field( "description" ).alias("Description")
			.field( "price" ).alias("Price").build();
	}
}