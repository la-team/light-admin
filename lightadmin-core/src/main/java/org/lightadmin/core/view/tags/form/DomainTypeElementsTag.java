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

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.view.tags.AbstractAutowiredTag;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;

@SuppressWarnings("unchecked")
public class DomainTypeElementsTag extends AbstractAutowiredTag {

    @Autowired(required = true)
    private GlobalAdministrationConfiguration configuration;

    private Class<?> domainType;

    private String idVar;
    private String stringRepresentationVar;

    @Override
    public void doTag() throws JspException, IOException {

        DomainTypeBasicConfiguration domainTypeConfiguration = configuration.forDomainType(domainType);
        Assert.notNull(domainTypeConfiguration, "<domainTypeConfiguration> not found for association");

        // TODO: Implement configurable ordering
        List allElements = domainTypeConfiguration.getRepository().findAll();
        allElements = sortByNaturalOrder(allElements, domainTypeConfiguration);

        PersistentProperty idAttribute = domainTypeConfiguration.getPersistentEntity().getIdProperty();
        EntityNameExtractor<Object> nameExtractor = domainTypeConfiguration.getEntityConfiguration().getNameExtractor();
        JspContext jspContext = getJspContext();
        JspFragment tagBody = getJspBody();
        for (Object element : allElements) {
            BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(element);

            jspContext.setAttribute(idVar, beanWrapper.getPropertyValue(idAttribute.getName()));
            jspContext.setAttribute(stringRepresentationVar, exceptionAwareNameExtractor(nameExtractor, domainTypeConfiguration).apply(element));
            tagBody.invoke(null);
        }
    }

    private List sortByNaturalOrder(List elements, DomainTypeBasicConfiguration elementTypeConfiguration) {

        EntityNameExtractor<Object> nameExtractor = elementTypeConfiguration.getEntityConfiguration().getNameExtractor();

        List<Pair<Object, String>> elementNamePairs = newArrayList();
        for (Object element : elements) {
            final String name = exceptionAwareNameExtractor(nameExtractor, elementTypeConfiguration).apply(element);
            elementNamePairs.add(new Pair<Object, String>(element, name));
        }

        Collections.sort(elementNamePairs, new Comparator<Pair<Object, String>>() {
            @Override
            public int compare(Pair<Object, String> pair1, Pair<Object, String> pair2) {
                return pair1.getSecond().compareToIgnoreCase(pair2.getSecond());
            }
        });

        List sortedElements = new ArrayList<Object>(elementNamePairs.size());
        for (Pair<Object, String> elementNamePair : elementNamePairs) {
            sortedElements.add(elementNamePair.getFirst());
        }
        return sortedElements;
    }

    public void setDomainType(Class<?> type) {
        this.domainType = type;
    }

    public void setIdVar(String idVar) {
        this.idVar = idVar;
    }

    public void setStringRepresentationVar(String stringRepresentationVar) {
        this.stringRepresentationVar = stringRepresentationVar;
    }

}
