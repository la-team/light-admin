package org.lightadmin.core.config.domain.unit.visitor;

import com.google.common.collect.FluentIterable;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.HierarchicalConfigurationUnit;

import java.util.Set;

public class HierarchicalConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<HierarchicalConfigurationUnit> {

    private ConfigurationUnit parentUnit;

    public HierarchicalConfigurationUnitVisitor(ConfigurationUnit parentUnit) {
        this.parentUnit = parentUnit;
    }

    @Override
    protected void visitInternal(HierarchicalConfigurationUnit configurationUnit) {
        if (parentUnit instanceof FieldSetConfigurationUnit && configurationUnit instanceof FieldSetConfigurationUnit) {
            Set<FieldMetadata> parentDecls = ((FieldSetConfigurationUnit) parentUnit).getFields();

            FluentIterable<PersistentFieldMetadata> localDecls = FluentIterable.from(((FieldSetConfigurationUnit) configurationUnit).getFields()).filter(PersistentFieldMetadata.class);
            for (PersistentFieldMetadata localDecl : localDecls) {
                PersistentFieldMetadata parentDecl = FieldMetadataUtils.getPersistentField(parentDecls, localDecl.getField());
                if (parentDecl != null) {
                    localDecl.inheritFrom(parentDecl);
                }
            }
        }
    }
}