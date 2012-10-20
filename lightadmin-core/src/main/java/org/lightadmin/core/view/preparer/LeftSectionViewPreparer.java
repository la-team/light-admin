package org.lightadmin.core.view.preparer;

import com.google.common.collect.Collections2;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.beans.MenuItem;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;

import java.util.Collection;

public class LeftSectionViewPreparer extends ConfigurationAwareViewPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final GlobalAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );

		addAttribute( attributeContext, "menuItems", menuItems( configuration.getDomainTypeConfigurations().values() ) );
	}

	private Collection<MenuItem> menuItems( Collection<DomainTypeAdministrationConfiguration> configurations ) {
		return Collections2.transform( configurations, DomainConfigToMenuItemTransformer.INSTANCE );
	}
}