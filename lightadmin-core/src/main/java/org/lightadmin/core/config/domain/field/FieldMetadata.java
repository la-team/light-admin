package org.lightadmin.core.config.domain.field;

public interface FieldMetadata extends Identifiable, Nameable {

    boolean isSortable();

    int getSortOrder();

    void setSortOrder(int sortOrder);

    Object getValue(Object entity);

    boolean isDynamic();
}