package org.lightadmin.core.view.preparer;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class ViewContextPreparer implements ViewPreparer {

	@Autowired
	private GlobalAdministrationConfiguration configuration;

	public final void execute( TilesRequestContext tilesContext, AttributeContext attributeContext ) {
		execute( tilesContext, attributeContext, configuration );
		Class<?> domainType = ( Class<?> ) attributeFromRequest( tilesContext, "domainType" );
		if ( domainType != null ) {
			execute( tilesContext, attributeContext, configuration.forDomainType( domainType ) );
		}
	}

	protected void execute( TilesRequestContext tilesContext, AttributeContext attributeContext, GlobalAdministrationConfiguration configuration ) {
	}

	protected void execute( TilesRequestContext tilesContext, AttributeContext attributeContext, DomainTypeAdministrationConfiguration configuration ) {
		addAttribute( attributeContext, "domainType", configuration.getDomainType(), true );
		addAttribute( attributeContext, "domainTypeName", configuration.getDomainTypeName(), true );
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
}