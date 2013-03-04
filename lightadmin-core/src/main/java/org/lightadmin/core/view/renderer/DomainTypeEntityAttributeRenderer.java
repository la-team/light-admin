package org.lightadmin.core.view.renderer;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.web.util.ApplicationUrlResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.Writer;

import static org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorExceptionAware.exceptionAwareNameExtractor;

public class DomainTypeEntityAttributeRenderer extends AbstractAttributeRenderer {

	private final DomainTypeEntityMetadata domainTypeEntityMetadata;

	public DomainTypeEntityAttributeRenderer( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		this.domainTypeEntityMetadata = domainTypeEntityMetadata;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	protected void write( final Object value, final Writer writer ) throws IOException {
		final DomainTypeAdministrationConfiguration domainTypeConfiguration = domainTypeConfiguration( domainTypeEntityMetadata.getDomainType() );

		final String entityViewUrl = entityShowUrl( value, domainTypeConfiguration );

		final EntityNameExtractor<Object> nameExtractor = domainTypeConfiguration.getEntityConfiguration().getNameExtractor();

		final String stringValue = exceptionAwareNameExtractor( nameExtractor ).apply( value );

		writer.write( String.format( "<a href=\"%s\">%s</a>", entityViewUrl, stringValue ) );
	}

	private String entityShowUrl( final Object value, final DomainTypeAdministrationConfiguration domainTypeConfiguration ) {
		final String domainBaseUrl = ApplicationUrlResolver.domainBaseUrl( domainTypeConfiguration.getDomainTypeName() );
		final Object entityId = domainTypeEntityMetadata.getIdAttribute().getValue( value );

		// TODO: max: Consider rewriting using PageContext
		final ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		servletUriComponentsBuilder.path( domainBaseUrl + "/" + entityId );

		return servletUriComponentsBuilder.build().getPath();
	}
}