package org.lightadmin.core.view.preparer;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;

public class ListViewPreparer extends ViewContextPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final DomainTypeAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );

		attributeContext.putAttribute( "listColumns", new Attribute( configuration.getListColumns() ) );
	}
}