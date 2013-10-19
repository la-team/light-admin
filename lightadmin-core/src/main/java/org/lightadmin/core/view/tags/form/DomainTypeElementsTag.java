package org.lightadmin.core.view.tags.form;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.view.tags.AbstractAutowiredTag;
import org.springframework.beans.factory.annotation.Autowired;
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

        DomainTypeAttributeMetadata idAttribute = domainTypeConfiguration.getDomainTypeEntityMetadata().getIdAttribute();
        EntityNameExtractor<Object> nameExtractor = domainTypeConfiguration.getEntityConfiguration().getNameExtractor();
        JspContext jspContext = getJspContext();
        JspFragment tagBody = getJspBody();
        for (Object element : allElements) {
            jspContext.setAttribute(idVar, idAttribute.getValue(element));
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
