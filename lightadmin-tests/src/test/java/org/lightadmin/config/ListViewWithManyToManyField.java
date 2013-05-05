package org.lightadmin.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.FieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.test.model.TestCustomer;

@SuppressWarnings("unused")
@Administration(TestCustomer.class)
public class ListViewWithManyToManyField {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder.screenName( "Administration of Test Customers Administration" ).build();
	}

	public static FieldSetConfigurationUnit listView( final FieldSetConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder.field( "firstname" ).caption( "First Name" ).field( "lastname" ).caption( "Last Name" ).field( "discountPrograms" ).caption( "Discount Programs" ).build();
	}
}
