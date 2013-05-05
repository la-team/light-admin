package org.lightadmin.core.view.preparer;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.beans.MenuItem;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.view.preparer.support.DomainConfigToMenuItemTransformer;
import org.lightadmin.core.view.preparer.support.MenuItemComparator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.sort;

public class DashboardViewPreparer extends ConfigurationAwareViewPreparer {

	@Override
	protected void execute( final TilesRequestContext tilesContext, final AttributeContext attributeContext, final GlobalAdministrationConfiguration configuration ) {
		super.execute( tilesContext, attributeContext, configuration );

		addAttribute( attributeContext, "dashboardDomainTypes", dashboardDomainTypes( configuration.getManagedDomainTypeConfigurations().values() ) );
	}

	private Collection<Pair<MenuItem, Long>> dashboardDomainTypes( Collection<DomainTypeAdministrationConfiguration> configurations ) {
		final List<Pair<MenuItem, Long>> domainTypeItems = newLinkedList();
		for ( DomainTypeAdministrationConfiguration configuration : configurations ) {
			domainTypeItems.add( Pair.create( menuItem( configuration ), configuration.getRepository().count() ) );
		}

		sort( domainTypeItems, new Comparator<Pair<MenuItem, Long>>() {
			@Override
			public int compare( final Pair<MenuItem, Long> menuItemPair1, final Pair<MenuItem, Long> menuItemPair2 ) {
				return MenuItemComparator.INSTANCE.compare( menuItemPair1.first, menuItemPair2.first );
			}
		} );

		return domainTypeItems;
	}

	private MenuItem menuItem( final DomainTypeAdministrationConfiguration configuration ) {
		return DomainConfigToMenuItemTransformer.INSTANCE.apply( configuration );
	}
}