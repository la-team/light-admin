package org.lightadmin.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.FieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.test.model.TestDiscountProgram;

@Administration(TestDiscountProgram.class)
public class ManyToManyEntityConfiguration {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder.screenName( "Discounts Administration" ).build();
	}

	public static FieldSetConfigurationUnit listView( FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "name" ).caption( "Name" ).field( "customers" ).caption( "Customers" ).build();
	}
}
