package org.lightadmin.demo.config;

import com.google.common.base.Function;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.view.support.configuration.EntityConfiguration;
import org.lightadmin.core.view.support.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.view.support.context.ScreenContext;
import org.lightadmin.core.view.support.context.ScreenContextBuilder;
import org.lightadmin.core.view.support.fragment.Fragment;
import org.lightadmin.core.view.support.fragment.FragmentBuilder;
import org.lightadmin.demo.model.Address;

@Administration( Address.class )
public class AddressAdministration {

	public static EntityConfiguration configuration( EntityConfigurationBuilder configurationBuilder ) {
		return configurationBuilder.nameExtractor( addressNameExtractor() ).build();
	}

	public static ScreenContext screenContext( ScreenContextBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Addresses Administration" )
			.menuName( "Addresses" ).build();
	}

	public static Fragment listView( FragmentBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field("country").alias( "Country")
			.field("city").alias("City")
			.field("street").alias("Street").build();
	}

	private static Function<?, String> addressNameExtractor() {
		return new Function<Address, String>() {
			@Override
			public String apply( final Address address ) {
				return String.format( "%s, %s, %s", address.getCountry(), address.getCity(), address.getStreet() );
			}
		};
	}
}