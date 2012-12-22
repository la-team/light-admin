package org.lightadmin.core.view.tags.form;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import javax.persistence.metamodel.Attribute;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Date;

public class EditControlDispatcherTag extends SimpleTagSupport {

	private static final String DISABLED = "disabled";

	private DomainTypeAttributeMetadata attributeMetadata;

	private JspFragment simpleEditControl;
	private JspFragment booleanEditControl;
	private JspFragment dateEditControl;
	private JspFragment n2oneEditControl;
	private JspFragment n2manyEditControl;
	private JspFragment mapEditControl;

	@Override
	public void doTag() throws JspException, IOException {
		JspContext context = getJspContext();
		Class<?> attrType = attributeMetadata.getType();
		JspFragment worker;
		if (attributeMetadata.isAssociation() || attributeMetadata.getAttribute().getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ) {
			if (attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike()) {
				worker = n2manyEditControl;
			} else {
				worker = n2oneEditControl;
			}
		} else if (attributeMetadata.isMapLike()) {
			worker = mapEditControl;
		} else if (Boolean.class.equals( booleanEditControl )) {
			worker = booleanEditControl;
		} else if (Date.class.isAssignableFrom( attrType )) {
			worker = dateEditControl;
		} else if (String.class.equals(attrType) || Number.class.isAssignableFrom(attrType)) {
			worker = simpleEditControl;
		} else {
			// An embedded attribute ?
			worker = simpleEditControl;
		}
		try {
			worker.invoke(null);
		} finally {
			context.removeAttribute(DISABLED);
		}
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
