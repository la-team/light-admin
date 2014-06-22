package org.lightadmin.core.config.domain.unit.visitor;

public interface VisitableConfigurationUnit {

    void accept(ConfigurationUnitVisitor<VisitableConfigurationUnit> configurationUnitVisitor);

}