package org.lightadmin.core.config.domain.unit;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

import java.io.Serializable;

public interface ConfigurationUnit extends Serializable {

	DomainConfigurationUnitType getDomainConfigurationUnitType();

	Class<?> getDomainType();
}