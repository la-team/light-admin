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
package org.lightadmin.core.view.tags.form;

import org.lightadmin.core.web.util.WebContextUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.tags.form.AbstractHtmlInputElementTag;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.SelectTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.jsp.JspException;
import java.io.Serializable;

public class DynamicHtmlElementTag extends AbstractHtmlInputElementTag {

    private PersistentProperty persistentProperty;

    @Override
    protected int writeTagContent(final TagWriter tagWriter) throws JspException {
        if (persistentProperty.isMap()) {
            return SKIP_BODY;
        }
        if (persistentProperty.isCollectionLike()) {
            return selectTag().doStartTag();
        }

        return inputTag().doStartTag();
    }

    private SelectTag selectTag() {
        SelectTag selectTag = new SelectTag() {
            @Override
            protected Object getItems() {
                try {
                    return getBindStatus().getValue();
                } catch (JspException e) {
                    // nop
                }
                return null;
            }
        };
        selectTag.setMultiple(true);
        selectTag.setCssClass(getCssClass());
        selectTag.setPageContext(pageContext);
        selectTag.setParent(getParent());
        selectTag.setPath(persistentProperty.getName());

        final Class<?> optionElementClass = persistentProperty.getActualType();

        final String idAttributeName = entityInformation(optionElementClass).getIdAttribute().getName();

        selectTag.setItemLabel(idAttributeName);
        selectTag.setItemValue(idAttributeName);
        return selectTag;
    }

    private InputTag inputTag() {
        InputTag inputTag = new InputTag();
        inputTag.setCssClass(getCssClass());
        inputTag.setPath(persistentProperty.getName());
        inputTag.setPageContext(pageContext);
        inputTag.setParent(getParent());
        return inputTag;
    }

    public PersistentProperty getPersistentProperty() {
        return persistentProperty;
    }

    public void setPersistentProperty(final PersistentProperty persistentProperty) {
        this.persistentProperty = persistentProperty;
    }

    private Class<? extends Serializable> idType(final Class<?> typeClass) {
        return entityInformation(typeClass).getIdType();
    }

    private JpaEntityInformation<?, ?> entityInformation(final Class<?> typeClass) {
        return JpaEntityInformationSupport.getMetadata(typeClass, entityManager());
    }

    private WebApplicationContext webApplicationContext() {
        return WebContextUtils.getWebApplicationContext(pageContext.getServletContext());
    }

    private EntityManager entityManager() {
        return webApplicationContext().getBean(EntityManagerFactory.class).createEntityManager();
    }
}