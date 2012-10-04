package org.lightadmin.core.view.renderer;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Writer;

class IterableAttributeRenderer implements AttributeRenderer {

	private final AbstractAttributeRenderer attributeRenderer;

	public IterableAttributeRenderer( final AbstractAttributeRenderer attributeRenderer ) {
		this.attributeRenderer = attributeRenderer;
	}

	@Override
	public void render( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject, final PageContext pageContext ) throws IOException {
		final Writer writer = pageContext.getOut();

		final Iterable value = ( Iterable ) attributeMetadata.getValue( domainTypeObject );

		for ( Object element : value ) {
			attributeRenderer.write( element, writer );
			attributeRenderer.write( "<br/>", writer );
		}
	}
}