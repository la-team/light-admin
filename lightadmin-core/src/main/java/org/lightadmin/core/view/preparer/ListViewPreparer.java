package org.lightadmin.core.view.preparer;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.view.support.fragment.TableFragment;

public class ListViewPreparer extends ConfigurationAwareViewPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final DomainTypeAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );

		final TableFragment listViewFragment = ( TableFragment ) configuration.getListViewFragment();

		addAttribute( attributeContext, "listColumns", listViewFragment.getColumns() );

		addAttribute( attributeContext, "scopes", configuration.getScopes() );

		addAttribute( attributeContext, "filters", configuration.getFilters() );
	}
}