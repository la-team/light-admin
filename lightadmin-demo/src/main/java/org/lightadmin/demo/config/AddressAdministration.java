package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
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

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "country" ).alias( "Country" )
							  .field( "city" ).alias( "City" )
							  .field( "street" ).alias( "Street" ).build();
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