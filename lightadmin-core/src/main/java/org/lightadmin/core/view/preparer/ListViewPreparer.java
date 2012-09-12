package org.lightadmin.core.view.preparer;

import org.apache.commons.lang.StringUtils;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.jsp.context.JspTilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import java.util.Collection;
import java.util.Set;

public class ListViewPreparer implements ViewPreparer {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Override
	public void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext ) {
		final ServletRequest request = ( ( JspTilesRequestContext ) tilesContext ).getPageContext().getRequest();

		final String domainType = ( String ) request.getAttribute( "domainType" );

		final Collection<DomainTypeAdministrationConfiguration> configurations = globalAdministrationConfiguration.getDomainTypeConfigurations().values();
		for ( DomainTypeAdministrationConfiguration configuration : configurations ) {
			if ( StringUtils.equalsIgnoreCase( domainType, configuration.getDomainTypeName() )) {
				Set<Pair<String, String>> listColumns = configuration.getListColumns();
				tilesContext.getRequestScope().put( "listColumns", listColumns );
			}
		}
	}
}