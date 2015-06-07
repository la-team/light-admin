package org.lightadmin.config;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.test.model.ConfigurationValidationTestEntity;

public class UnsupportedFieldTypeAdministration extends AdministrationConfiguration<ConfigurationValidationTestEntity> {

	public FieldSetConfigurationUnit quickView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
		return fragmentBuilder
				.field( "name" ).caption( "Name" )
				.field( "unsupportedType" ).caption( "Field Of Unsupported Type" ).build();
	}
}
