package org.lightadmin.demo.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.FieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.common.PersistentFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.demo.model.DiscountProgram;

@SuppressWarnings( "unused" )

@Administration( DiscountProgram.class )
public class DiscountProgramAdministration {

	public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
		return configurationBuilder.nameField( "name" ).build();
	}

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder.screenName( "Discounts Administration" ).menuName( "Discounts" ).build();
	}

	public static FieldSetConfigurationUnit listView( FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "name" ).caption( "Name" ).build();
	}

	public static FieldSetConfigurationUnit quickView( FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "name" ).caption( "Name" ).build();
	}

	public static FieldSetConfigurationUnit showView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "name" ).caption( "Name" ).build();
	}

	public static FieldSetConfigurationUnit formView( final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "name" ).caption( "Name" ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder.filter( "Name", "name" ).build();
	}
}