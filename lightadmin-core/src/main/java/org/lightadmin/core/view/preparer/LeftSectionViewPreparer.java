package org.lightadmin.core.view.preparer;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.Pair;

import java.util.Collection;
import java.util.Set;

public class LeftSectionViewPreparer extends ViewContextPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final GlobalAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );

		final Set<Pair<String, String>> result = Sets.newLinkedHashSet();

		final Collection<DomainTypeAdministrationConfiguration> configurations = configuration.getDomainTypeConfigurations().values();
		for ( DomainTypeAdministrationConfiguration domainConfiguration : configurations ) {
			final String domainTypeName = domainConfiguration.getDomainTypeName();
			final String pageUrl = "/domain/" + domainTypeName;
			result.add( Pair.stringPair( domainTypeName, pageUrl ) );
		}

		attributeContext.putAttribute( "menuItems", new Attribute( result ) );
	}
}