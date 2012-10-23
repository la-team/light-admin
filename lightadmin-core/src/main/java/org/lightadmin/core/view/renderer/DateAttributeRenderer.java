package org.lightadmin.core.view.renderer;

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
}