package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

public interface ConfigurationUnitPostProcessor {

	ConfigurationUnit postProcess( ConfigurationUnit configurationUnit );

}