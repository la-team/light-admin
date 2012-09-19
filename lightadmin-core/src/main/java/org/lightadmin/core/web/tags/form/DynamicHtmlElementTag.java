package org.lightadmin.core.web.tags.form;

import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.web.servlet.tags.form.AbstractHtmlInputElementTag;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

public class DynamicHtmlElementTag extends AbstractHtmlInputElementTag {

	private AttributeMetadata attributeMetadata;

	@Override
	protected int writeTagContent( final TagWriter tagWriter ) throws JspException {
		if ( attributeMetadata.isCollectionLike() || attributeMetadata.isMapLike() || attributeMetadata.isSetLike() ) {
			return SKIP_BODY;
		}

		return inputTag().doStartTag();
	}

	private InputTag inputTag() {
		InputTag inputTag = new InputTag();
		inputTag.setCssClass( getCssClass() );
		inputTag.setPath( attributeMetadata.name() );
		inputTag.setPageContext( pageContext );
		inputTag.setParent( getParent() );
		return inputTag;
	}

	public AttributeMetadata getAttributeMetadata() {
		return attributeMetadata;
	}

	public void setAttributeMetadata( final AttributeMetadata attributeMetadata ) {
		this.attributeMetadata = attributeMetadata;
	}
}