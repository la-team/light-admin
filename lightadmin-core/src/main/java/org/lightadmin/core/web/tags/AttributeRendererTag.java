package org.lightadmin.core.web.tags;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.view.renderer.AttributeRenderer;
import org.lightadmin.core.view.renderer.AttributeRendererFactory;
import org.lightadmin.core.view.renderer.BasicAttributeRendererFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import javax.servlet.jsp.JspException;
import java.io.IOException;

public class AttributeRendererTag extends RequestContextAwareTag {

	private DomainTypeAttributeMetadata attributeMetadata;

	private Object entity;

	@Override
	protected final int doStartTagInternal() throws JspException, IOException {
		final AttributeRendererFactory rendererFactory = attributeRendererFactory();

		final AttributeRenderer renderer = rendererFactory.getRenderer( attributeMetadata );

		renderer.render( attributeMetadata, entity, pageContext );

		return SKIP_BODY;
	}

	private BasicAttributeRendererFactory attributeRendererFactory() {
		final ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext( pageContext.getServletContext() );

		return applicationContext.getBean( BasicAttributeRendererFactory.class );
	}

	public DomainTypeAttributeMetadata getAttributeMetadata() {
		return attributeMetadata;
	}

	public void setAttributeMetadata( final DomainTypeAttributeMetadata attributeMetadata ) {
		this.attributeMetadata = attributeMetadata;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity( final Object entity ) {
		this.entity = entity;
	}
}