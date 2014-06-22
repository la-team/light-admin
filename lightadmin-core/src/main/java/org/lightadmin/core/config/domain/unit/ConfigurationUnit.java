package org.lightadmin.core.config.domain.unit;

import java.io.Serializable;

public interface ConfigurationUnit extends Serializable {

    DomainConfigurationUnitType getDomainConfigurationUnitType();

    Class<?> getDomainType();
}