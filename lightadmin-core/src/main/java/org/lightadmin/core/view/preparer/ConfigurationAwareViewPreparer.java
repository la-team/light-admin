package org.lightadmin.core.view.preparer;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfigurationAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class ConfigurationAwareViewPreparer implements ViewPreparer, GlobalAdministrationConfigurationAware {

	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	public final void execute( TilesRequestContext tilesContext, AttributeContext attributeContext ) {
		execute( tilesContext, attributeContext, globalAdministrationConfiguration );
		DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = domainTypeConfiguration( tilesContext );
		if ( domainTypeAdministrationConfiguration != null ) {
			execute( tilesContext, attributeContext, domainTypeAdministrationConfiguration );
		}
	}

	protected void execute( TilesRequestContext tilesContext, AttributeContext attributeContext, GlobalAdministrationConfiguration configuration ) {
	}

	protected void execute( TilesRequestContext tilesContext, AttributeContext attributeContext, DomainTypeAdministrationConfiguration configuration ) {
		addAttribute( attributeContext, "domainTypeAdministrationConfiguration", configuration, true );
	}

	private DomainTypeAdministrationConfiguration domainTypeConfiguration( final TilesRequestContext tilesContext ) {
		return ( DomainTypeAdministrationConfiguration ) attributeFromRequest( tilesContext, "domainTypeAdministrationConfiguration" );
	}

	private Object attributeFromRequest( TilesRequestContext tilesContext, String attributeName ) {
		final ServletTilesRequestContext servletTilesRequestContext = ServletUtil.getServletRequest( tilesContext );
		final HttpServletRequest httpServletRequest = servletTilesRequestContext.getRequest();

		return httpServletRequest.getAttribute( attributeName );
	}

	protected void addAttribute( AttributeContext attributeContext, String attributeName, Object attributeValue ) {
		addAttribute( attributeContext, attributeName, attributeValue, false );
	}

	protected void addAttribute( AttributeContext attributeContext, String attributeName, Object attributeValue, boolean cascade ) {
		attributeContext.putAttribute( attributeName, new Attribute( attributeValue ), cascade );
	}

	@Override
	@Autowired
	public void setGlobalAdministrationConfigurationAware( final GlobalAdministrationConfiguration configuration ) {
		this.globalAdministrationConfiguration = configuration;
	}
}