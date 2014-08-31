/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.config.domain.field;

import com.google.common.base.Objects;
import org.apache.commons.collections15.Factory;
import org.lightadmin.api.config.utils.FieldValueRenderer;

import javax.servlet.jsp.tagext.SimpleTag;
import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractFieldMetadata implements FieldMetadata, Serializable {

    private String name;

    private int order;

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
    public void setSortOrder(int sortOrder) {
        this.order = sortOrder;
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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", name)
                .add("order", order)
                .add("uuid", uuid)
                .toString();
    }
}
