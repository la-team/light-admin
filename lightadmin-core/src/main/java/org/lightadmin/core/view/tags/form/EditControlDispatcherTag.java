package org.lightadmin.core.view.tags.form;

import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType;

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
		JspFragment worker;
		switch ( DomainTypeAttributeType.by( attributeMetadata )) {
		case ASSOC_MULTI:
			worker = n2manyEditControl;
			break;
		case ASSOC:
			worker = n2oneEditControl;
			break;
		case MAP:
			worker = mapEditControl;
			break;
		case BOOL:
			worker = booleanEditControl;
			break;
		case DATE:
			worker = dateEditControl;
			break;
		default:
			worker = simpleEditControl;
			break;
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
