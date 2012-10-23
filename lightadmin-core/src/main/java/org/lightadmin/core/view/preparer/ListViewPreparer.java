package org.lightadmin.core.view.preparer;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;

public class ListViewPreparer extends ConfigurationAwareViewPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final DomainTypeAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );

		addAttribute( attributeContext, "fields", configuration.getListViewFragment().getFields() );

		addAttribute( attributeContext, "scopes", configuration.getScopes() );

		addAttribute( attributeContext, "filters", configuration.getFilters() );
	}
}