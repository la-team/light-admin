package org.lightadmin.core.web.tags;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.view.renderer.AttributeRenderer;
import org.lightadmin.core.view.renderer.AttributeRendererFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

public abstract class AttributeRendererFactoryAwareTag extends TagSupport implements TryCatchFinally {

	protected AttributeRendererFactory attributeRendererFactory;

	@Override
	public final int doStartTag() throws JspException {
		try {
			this.attributeRendererFactory = applicationContext().getBean( AttributeRendererFactory.class );

			return doStartTagInternal();
		} catch ( Exception ex ) {
			throw new JspTagException( ex.getMessage() );
		}
	}

	protected AttributeRenderer rendererFor( DomainTypeAttributeMetadata attributeMetadata ) {
		return attributeRendererFactory.getRenderer( attributeMetadata );
	}

	/**
	 * Called by doStartTag to perform the actual work.
	 *
	 * @return same as TagSupport.doStartTag
	 * @throws Exception any exception, any checked one other than
	 *                   a JspException gets wrapped in a JspException by doStartTag
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag
	 */
	protected abstract int doStartTagInternal() throws Exception;

	@Override
	public void doCatch( Throwable throwable ) throws Throwable {
		throw throwable;
	}

	@Override
	public void doFinally() {
		this.attributeRendererFactory = null;
	}

	private WebApplicationContext applicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext( pageContext.getServletContext() );
	}
}