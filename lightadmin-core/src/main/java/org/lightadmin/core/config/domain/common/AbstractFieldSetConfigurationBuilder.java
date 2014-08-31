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
package org.lightadmin.core.config.domain.common;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections15.functors.PrototypeFactory;
import org.lightadmin.api.config.utils.Editors;
import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.core.config.domain.field.AbstractFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.renderer.EnumRenderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import javax.servlet.jsp.tagext.SimpleTag;

import static org.lightadmin.core.config.domain.field.FieldMetadataFactory.persistentField;
import static org.springframework.util.ObjectUtils.nullSafeEquals;
import static org.springframework.util.StringUtils.capitalize;

public abstract class AbstractFieldSetConfigurationBuilder<T extends ConfigurationUnit, B extends ConfigurationUnitBuilder<T>>
        extends DomainTypeConfigurationUnitBuilder<T> {

    protected FieldMetadata currentFieldMetadata;

    public AbstractFieldSetConfigurationBuilder(Class domainType) {
        super(domainType);
    }

    @SuppressWarnings("unchecked")
    public B field(String fieldName) {
        addCurrentFieldToUnit();

        currentFieldMetadata = persistentField(capitalize(fieldName), fieldName);

        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B caption(final String caption) {
        assertFieldMetadataIsNotNull();

        currentFieldMetadata.setName(caption);

        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B enumeration(EnumElement... elements) {

        assertFieldMetadataIsNotNull();
        assertFieldMetadataType(currentFieldMetadata, PersistentFieldMetadata.class);

        assertValidEnumElements(elements);

        PersistentFieldMetadata fieldMetadata = (PersistentFieldMetadata) currentFieldMetadata;
        fieldMetadata.setRenderer(new EnumRenderer(elements));

        withCustomControl(Editors.enumeration(elements));

        return (B) this;
    }

    @SuppressWarnings("unchecked")
    protected B withCustomControl(SimpleTag customControl) {

        assertFieldMetadataType(currentFieldMetadata, AbstractFieldMetadata.class);

        try {
            BeanUtils.setProperty(customControl, "field", currentFieldMetadata.getUuid());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        AbstractFieldMetadata fieldMetadata = (AbstractFieldMetadata) currentFieldMetadata;
        fieldMetadata.setCustomControlFactory(PrototypeFactory.getInstance(customControl));

        return (B) this;
    }

    protected static void assertFieldMetadataType(FieldMetadata currentFieldMetadata, Class<?> type) {
        if (currentFieldMetadata == null || !type.isAssignableFrom(currentFieldMetadata.getClass())) {
            throw new RuntimeException("Field is not defined or wrong type.");
        }
    }

    protected void assertFieldMetadataIsNotNull() {
        if (currentFieldMetadata == null) {
            throw new RuntimeException("Field is not defined yet.");
        }
    }

    protected void assertValidEnumElements(final EnumElement[] elements) {
        for (int i = 0, n = elements.length; i < n; i++) {
            Object baseVal = elements[i].getValue();
            for (int j = i + 1; j < n; j++) {
                Object cmpVal = elements[j].getValue();
                if (nullSafeEquals(baseVal, cmpVal)) {
                    throw new RuntimeException("Non-unique value of EnumElement: " + baseVal);
                }
            }
        }
    }

    protected abstract void addCurrentFieldToUnit();

}
