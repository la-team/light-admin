package org.lightadmin.core.view.tags.form;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class EditControlDispatcherTag extends SimpleTagSupport {

	private static final String DISABLED = "disabled";

	private DomainTypeAttributeMetadata attributeMetadata;

	private JspFragment simpleEditControl;
	private JspFragment numberEditControl;
	private JspFragment booleanEditControl;
	private JspFragment fileEditControl;
	private JspFragment dateEditControl;
	private JspFragment n2oneEditControl;
	private JspFragment n2manyEditControl;
	private JspFragment mapEditControl;

	@Override
	public void doTag() throws JspException, IOException {
		JspContext context = getJspContext();
		JspFragment worker;
		switch ( attributeMetadata.getAttributeType() ) {
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
			case NUMBER_INTEGER:
				worker = numberEditControl;
				break;
			case NUMBER_FLOAT:
				worker = numberEditControl;
				break;
			case STRING:
				worker = simpleEditControl;
				break;
			case FILE:
				worker = fileEditControl;
				break;
			default:
				worker = simpleEditControl;
				break;
		}
		try {
			worker.invoke( null );
		} finally {
			context.removeAttribute( DISABLED );
		}
	}

	public void setAttributeMetadata( DomainTypeAttributeMetadata attributeMetadata ) {
		this.attributeMetadata = attributeMetadata;
	}

	public void setSimpleEditControl( JspFragment control ) {
		this.simpleEditControl = control;
	}

	public void setNumberEditControl( JspFragment control ) {
		this.numberEditControl = control;
	}

	public void setBooleanEditControl( JspFragment control ) {
		this.booleanEditControl = control;
	}

	public void setDateEditControl( JspFragment control ) {
		this.dateEditControl = control;
	}

	public void setN2oneEditControl( JspFragment control ) {
		this.n2oneEditControl = control;
	}

	public void setN2manyEditControl( JspFragment control ) {
		this.n2manyEditControl = control;
	}

	public void setMapEditControl( JspFragment control ) {
		this.mapEditControl = control;
	}

	public void setFileEditControl( final JspFragment fileEditControl ) {
		this.fileEditControl = fileEditControl;
	}
}