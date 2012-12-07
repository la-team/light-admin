package org.lightadmin.core.view.tags.form;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

public class EditControlDispatcherTag extends SimpleTagSupport {

    private DomainTypeAttributeMetadata attributeMetadata;

    private JspFragment simpleEditControl;
    private JspFragment booleanEditControl;
    private JspFragment dateEditControl;
    private JspFragment n2oneEditControl;
    private JspFragment n2manyEditControl;
    private JspFragment mapEditControl;
    
    @Override
    public void doTag() throws JspException, IOException {
        JspFragment worker;
        Class<?> attrType = attributeMetadata.getType();
        if (attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike()) {
            worker = n2manyEditControl;
        } else if (attributeMetadata.isMapLike()) {
            worker = mapEditControl;
        } else if (Boolean.class.equals(booleanEditControl)) {
            worker = booleanEditControl;
        } else if (Date.class.isAssignableFrom(attrType)) {
            worker = dateEditControl;
        } else if (String.class.equals(attrType) || Number.class.isAssignableFrom(attrType)) {
            worker = simpleEditControl;
        } else {
            worker = n2oneEditControl;
        }
        worker.invoke(null);
    }

    public void setAttributeMetadata(DomainTypeAttributeMetadata attributeMetadata) {
        this.attributeMetadata = attributeMetadata;
    } 

    public void setSimpleEditControl(JspFragment control) {
        this.simpleEditControl = control;
    }

    public void setBooleanEditControl(JspFragment control) {
        this.booleanEditControl = control;
    }
    
    public void setDateEditControl(JspFragment control) {
        this.dateEditControl = control;
    }

    public void setN2oneEditControl(JspFragment control) {
        this.n2oneEditControl = control;
    }

    public void setN2manyEditControl(JspFragment control) {
        this.n2manyEditControl = control;
    }

    public void setMapEditControl(JspFragment control) {
        this.mapEditControl = control;
    }

}
