package org.lightadmin.core.config.domain.field;

import org.apache.commons.collections15.Factory;
import org.lightadmin.api.config.utils.FieldValueRenderer;

import javax.servlet.jsp.tagext.SimpleTag;
import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractFieldMetadata implements FieldMetadata, Serializable {

    private String name;

    private final int order;

    private final UUID uuid;

    private Factory<SimpleTag> customControlFactory;

    protected FieldValueRenderer renderer;

    protected AbstractFieldMetadata(final String name) {
        this(name, 0);
    }

    protected AbstractFieldMetadata(final String name, int order) {
        this.name = name;
        this.order = order;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSortOrder() {
        return order;
    }

    @Override
    public String getUuid() {
        return uuid.toString();
    }

    @Override
    public boolean isSortable() {
        return false;
    }

    public FieldValueRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(FieldValueRenderer renderer) {
        this.renderer = renderer;
    }

    public void setCustomControlFactory(Factory<SimpleTag> customControlFactory) {
        this.customControlFactory = customControlFactory;
    }

    public SimpleTag getCustomControl() {
        if (customControlFactory != null) {
            return customControlFactory.create();
        } else {
            return null;
        }
    }

    public void inheritFrom(AbstractFieldMetadata field) {
        if (renderer == null) {
            renderer = field.renderer;
        }
        if (customControlFactory == null) {
            customControlFactory = field.customControlFactory;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AbstractFieldMetadata that = (AbstractFieldMetadata) o;

        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}
