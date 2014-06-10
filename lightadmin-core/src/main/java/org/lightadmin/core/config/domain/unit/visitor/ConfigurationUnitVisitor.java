package org.lightadmin.core.config.domain.unit.visitor;

public interface ConfigurationUnitVisitor<T extends VisitableConfigurationUnit> {

    void visit(T configurationUnit);
}