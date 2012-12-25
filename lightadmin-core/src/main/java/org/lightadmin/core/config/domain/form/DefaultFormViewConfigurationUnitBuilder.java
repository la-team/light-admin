package org.lightadmin.core.config.domain.form;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.AbstractFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;


public class DefaultFormViewConfigurationUnitBuilder
	extends AbstractFieldSetConfigurationUnitBuilder<FieldSetConfigurationUnit, DefaultFormViewConfigurationUnitBuilder>
	implements FormViewConfigurationUnitBuilder {

	public DefaultFormViewConfigurationUnitBuilder(Class<?> domainType) {
		super(domainType, new DefaultFieldSetConfigurationUnit(domainType, DomainConfigurationUnitType.FORM_VIEW));
	}

}
