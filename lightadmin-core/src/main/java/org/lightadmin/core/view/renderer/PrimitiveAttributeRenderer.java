package org.lightadmin.core.view.renderer;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import java.io.IOException;
import java.io.Writer;

class PrimitiveAttributeRenderer extends AbstractAttributeRenderer {

	@Override
	protected void write( final Object value, final Writer writer ) throws IOException {
		writer.write( value.toString() );
	}

	@Override
	protected Object evaluateValue( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject ) {
		return attributeMetadata.getValue( domainTypeObject );
	}
}