package org.lightadmin.demo.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.FieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.common.PersistentFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.demo.model.Address;

@SuppressWarnings( "unused" )
@Administration( Address.class )
public class AddressAdministration {

	public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
		return configurationBuilder.nameExtractor( addressNameExtractor() ).build();
	}

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder.screenName( "Addresses Administration" )
								.menuName( "Addresses" ).build();
	}

	public static FieldSetConfigurationUnit listView( FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "country" ).caption( "Country" )
							.field( "city" ).caption( "City" )
							.field( "street" ).caption( "Street" ).build();
	}

	public static FieldSetConfigurationUnit quickView( FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "country" ).caption( "Country" )
							  .field( "city" ).caption( "City" ).build();
	}

	public static FieldSetConfigurationUnit showView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "country" ).caption( "Country" )
							.field( "city" ).caption( "City" )
							.field( "street" ).caption( "Street" ).build();
	}

	public static FieldSetConfigurationUnit formView( final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "country" ).caption( "Country" )
							.field( "city" ).caption( "City" )
							.field( "street" ).caption( "Street" ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.filter( "Country", "country" )
			.filter( "City", "city" )
			.filter( "Street", "street" ).build();
	}

	private static EntityNameExtractor<Address> addressNameExtractor() {
		return new EntityNameExtractor<Address>() {
			@Override
			public String apply( final Address address ) {
				return String.format( "%s, %s, %s", address.getCountry(), address.getCity(), address.getStreet() );
			}
		};
	}
}
