package org.lightadmin.core.view.renderer;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAttributeRenderer extends AbstractAttributeRenderer {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );

	@Override
	protected void write( final Object value, final Writer writer ) throws IOException {
		writer.write( DATE_FORMAT.format( ( Date ) value ) );
	}

	@Override
	protected Object evaluateValue( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject ) {
		return attributeMetadata.getValue( domainTypeObject );
	}
}