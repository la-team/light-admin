package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;

public interface ConfigurationUnitPostProcessor {

    ConfigurationUnit postProcess(ConfigurationUnit configurationUnit, ConfigurationUnits domainConfigUnits);

}
