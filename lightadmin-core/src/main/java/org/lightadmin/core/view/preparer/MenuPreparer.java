package org.lightadmin.core.view.preparer;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;

public class MenuPreparer implements ViewPreparer {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Override
	public void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext ) {
		final Set<Pair<String, String>> result = Sets.newLinkedHashSet();

		final Collection<DomainTypeAdministrationConfiguration> configurations = globalAdministrationConfiguration.getDomainTypeConfigurations().values();
		for ( DomainTypeAdministrationConfiguration configuration : configurations ) {
			final String domainTypeName = configuration.getDomainTypeName();
			final String pageUrl = "/" + StringUtils.uncapitalize( domainTypeName );
			result.add( Pair.stringPair( domainTypeName, pageUrl ) );
		}
		tilesContext.getRequestScope().put( "menuItems", result );
	}
}