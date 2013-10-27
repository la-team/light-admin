package org.lightadmin.core.view.tags;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import javax.servlet.jsp.JspException;
import java.io.IOException;

public class AttributeRendererTag extends AttributeRendererFactoryAwareTag {

    private DomainTypeAttributeMetadata attributeMetadata;

    private Object entity;

    @Override
    protected final int doStartTagInternal() throws JspException, IOException {
        rendererFor(attributeMetadata).render(attributeMetadata, entity, pageContext);

        return SKIP_BODY;
    }

    public DomainTypeAttributeMetadata getAttributeMetadata() {
        return attributeMetadata;
    }

    public void setAttributeMetadata(final DomainTypeAttributeMetadata attributeMetadata) {
        this.attributeMetadata = attributeMetadata;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(final Object entity) {
        this.entity = entity;
    }
}