package org.lightadmin.core.view.renderer;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.support.GlobalAdministrationConfigurationAware;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.io.Writer;

public abstract class AbstractAttributeRenderer implements AttributeRenderer, GlobalAdministrationConfigurationAware {

	protected GlobalAdministrationConfiguration configuration;

	@Override
	public void render( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject, final PageContext pageContext ) throws IOException {
		Object value = evaluateValue( attributeMetadata, domainTypeObject );

		write( value, pageContext.getOut() );
	}

	protected void write( final Object value, Writer writer ) throws IOException {
		writer.write( value.toString() );
	}

	protected Object evaluateValue( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject ) {
		return attributeMetadata.getValue( domainTypeObject );
	}

	protected DomainTypeAdministrationConfiguration domainTypeConfiguration( Class domainType ) {
		return configuration.forDomainType( domainType );
	}

	@Override
	public void setGlobalAdministrationConfiguration( final GlobalAdministrationConfiguration configuration ) {
		this.configuration = configuration;
	}
}