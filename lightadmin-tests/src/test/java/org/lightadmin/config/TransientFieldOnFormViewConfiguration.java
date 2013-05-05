package org.lightadmin.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.PersistentFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.test.model.TestOrder;

@SuppressWarnings("unused")
@Administration(TestOrder.class)
public class TransientFieldOnFormViewConfiguration {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder.screenName( "Administration of Test Order Domain" ).build();
	}

	public static FieldSetConfigurationUnit formView( PersistentFieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder.field( "name" ).caption( "Name" ).field( "orderTotal" ).caption( "Order Total" ).build();
	}
}
