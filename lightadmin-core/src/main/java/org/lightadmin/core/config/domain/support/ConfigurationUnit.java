package org.lightadmin.core.config.domain.support;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

import java.io.Serializable;

public interface ConfigurationUnit extends Serializable {

	DomainConfigurationUnitType getDomainConfigurationUnitType();

	Class<?> getDomainType();
}