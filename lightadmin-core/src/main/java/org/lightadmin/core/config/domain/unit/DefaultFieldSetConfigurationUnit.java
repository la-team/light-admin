package org.lightadmin.core.config.domain.unit;

import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.handler.FieldHandler;
import org.lightadmin.core.config.domain.unit.visitor.ConfigurationUnitVisitor;
import org.lightadmin.core.config.domain.unit.visitor.VisitableConfigurationUnit;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static com.google.common.collect.Sets.newTreeSet;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.persistentFields;
import static org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType.CONFIGURATION;
import static org.springframework.util.ClassUtils.isAssignableValue;

public class DefaultFieldSetConfigurationUnit extends DomainTypeConfigurationUnit
        implements FieldSetConfigurationUnit, HierarchicalConfigurationUnit, VisitableConfigurationUnit {

    private static final long serialVersionUID = 1L;

    private final DomainConfigurationUnitType configurationUnitType;

    private final AtomicInteger COUNTER = new AtomicInteger(1);

    private Set<FieldMetadata> fields = newLinkedHashSet();

    public DefaultFieldSetConfigurationUnit(Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        super(domainType);
        this.configurationUnitType = configurationUnitType;
    }

    @Override
    public void addField(FieldMetadata fieldMetadata) {
        if (isPrimaryKeyField(fieldMetadata)) {
            fieldMetadata.setSortOrder(0);
        } else {
            fieldMetadata.setSortOrder(COUNTER.getAndIncrement());
        }
        fields.add(fieldMetadata);
    }

    @Override
    public Set<FieldMetadata> getFields() {
        SortedSet<FieldMetadata> orderedFields = newTreeSet(new FieldMetadataUtils.FieldMetadataComparator());
        orderedFields.addAll(this.fields);
        return orderedFields;
    }

    @Override
    public Iterator<FieldMetadata> iterator() {
        return getFields().iterator();
    }

    @Override
    public boolean isEmpty() {
        return fields.isEmpty();
    }

    @Override
    public DomainConfigurationUnitType getDomainConfigurationUnitType() {
        return configurationUnitType;
    }

    @Override
    public DomainConfigurationUnitType getParentUnitType() {
        return CONFIGURATION;
    }

    @Override
    public void doWithFields(FieldHandler<FieldMetadata> handler) {
        for (FieldMetadata field : getFields()) {
            handler.doWithField(field);
        }
    }

    @Override
    public void doWithPersistentFields(FieldHandler<PersistentFieldMetadata> handler) {
        for (FieldMetadata field : persistentFields(getFields())) {
            handler.doWithField((PersistentFieldMetadata) field);
        }
    }

    @Override
    public void accept(ConfigurationUnitVisitor<VisitableConfigurationUnit> configurationUnitVisitor) {
        configurationUnitVisitor.visit(this);
    }

    private boolean isPrimaryKeyField(FieldMetadata fieldMetadata) {
        return isAssignableValue(PersistentFieldMetadata.class, fieldMetadata) && ((PersistentFieldMetadata) fieldMetadata).isPrimaryKey();
    }
}