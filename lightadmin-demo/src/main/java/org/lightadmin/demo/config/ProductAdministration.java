package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.form.FormViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnit;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
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

	public static ShowViewConfigurationUnit showView( final ShowViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "name" ).alias( "Name")
			.field( "description" ).alias("Description")
			.field( "price" ).alias("Price").build();
	}

	public static FieldSetConfigurationUnit formView( final FormViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
				.field( "name" ).alias( "Name")
				.field( "description" ).alias("Description")
				.field( "price" ).alias("Price").build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.filter( "Name", "name" )
			.filter( "Description", "description" )
			.filter( "Price", "price" ).build();
	}
}
