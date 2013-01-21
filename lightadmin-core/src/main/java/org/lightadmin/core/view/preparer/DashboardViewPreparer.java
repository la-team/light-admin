package org.lightadmin.core.view.preparer;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.beans.MenuItem;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.Pair;

import java.util.Collection;

import static com.google.common.collect.Lists.newLinkedList;

public class DashboardViewPreparer extends ConfigurationAwareViewPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final GlobalAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );

		addAttribute( attributeContext, "dashboardDomainTypes", dashboardDomainTypes( configuration.getManagedDomainTypeConfigurations().values() ) );
	}

	private Collection<Pair<MenuItem, Long>> dashboardDomainTypes( Collection<DomainTypeAdministrationConfiguration> configurations ) {
		final Collection<Pair<MenuItem, Long>> result = newLinkedList();
		for ( DomainTypeAdministrationConfiguration configuration : configurations ) {
			result.add( Pair.create( menuItem( configuration ), configuration.getRepository().count() ) );
		}
		return result;
	}

	private MenuItem menuItem( final DomainTypeAdministrationConfiguration configuration ) {
		return DomainConfigToMenuItemTransformer.INSTANCE.apply( configuration );
	}
}