package org.lightadmin.core.view.preparer;

import org.apache.tiles.beans.MenuItem;
import org.apache.tiles.beans.SimpleMenuItem;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.util.Transformer;

import static org.lightadmin.core.web.util.ApplicationUrlResolver.domainBaseUrl;

public class DomainConfigToMenuItemTransformer implements Transformer<DomainTypeAdministrationConfiguration, MenuItem> {

	public static final DomainConfigToMenuItemTransformer INSTANCE = new DomainConfigToMenuItemTransformer();

	private DomainConfigToMenuItemTransformer() {
	}

	@Override
	public MenuItem apply( final DomainTypeAdministrationConfiguration domainConfiguration ) {
		return menuItem( domainConfiguration.getScreenContext().getMenuItemName(), domainBaseUrl( domainConfiguration.getDomainTypeName() ) );
	}

	private MenuItem menuItem( final String name, final String url ) {
		MenuItem menuItem = new SimpleMenuItem();
		menuItem.setValue( name );
		menuItem.setLink( url );
		return menuItem;
	}
}