package org.lightadmin.core.view.preparer;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;

public class DashboardViewPreparer extends ConfigurationAwareViewPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final GlobalAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );
	}
}