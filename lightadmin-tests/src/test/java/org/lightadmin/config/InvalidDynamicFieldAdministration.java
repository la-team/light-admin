package org.lightadmin.config;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.test.model.TestOrder;

public class InvalidDynamicFieldAdministration extends AdministrationConfiguration<TestOrder> {

	public FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
		return fragmentBuilder
				.field( "name" ).caption( "Name" )
				.field( "customer.missingField" ).caption( "Missing Associated Entity Field" ).build();
	}

}
