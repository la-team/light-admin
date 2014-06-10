package org.lightadmin.core.config.domain.unit.visitor;

import static org.springframework.core.GenericTypeResolver.resolveTypeArgument;
import static org.springframework.util.ClassUtils.isAssignableValue;

public abstract class ParametrizedConfigurationUnitVisitor<T extends VisitableConfigurationUnit> implements ConfigurationUnitVisitor<VisitableConfigurationUnit> {

    private Class<?> TYPE = resolveTypeArgument(getClass(), ParametrizedConfigurationUnitVisitor.class);

    @Override
    @SuppressWarnings("unchecked")
    public void visit(VisitableConfigurationUnit configurationUnit) {
        if (isAssignableValue(TYPE, configurationUnit)) {
            visitInternal((T) configurationUnit);
        }
    }

    protected abstract void visitInternal(T configurationUnit);
}