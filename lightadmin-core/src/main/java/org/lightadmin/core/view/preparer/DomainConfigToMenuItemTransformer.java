package org.lightadmin.core.view.preparer;

import org.apache.tiles.beans.MenuItem;
import org.apache.tiles.beans.SimpleMenuItem;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.util.Transformer;

public class DomainConfigToMenuItemTransformer implements Transformer<DomainTypeAdministrationConfiguration, MenuItem> {

	public static final DomainConfigToMenuItemTransformer INSTANCE = new DomainConfigToMenuItemTransformer();

	private DomainConfigToMenuItemTransformer() {
	}

	@Override
	public MenuItem apply( final DomainTypeAdministrationConfiguration domainConfiguration ) {
		return menuItem( domainConfiguration.getScreenContext().getMenuItemName(), resolveDomainContextUrl( domainConfiguration ) );
	}

	private String resolveDomainContextUrl( DomainTypeAdministrationConfiguration domainConfiguration ) {
		return String.format( "/domain/%s", domainConfiguration.getDomainTypeName() );
	}

	private MenuItem menuItem( final String name, final String url ) {
		MenuItem menuItem = new SimpleMenuItem();
		menuItem.setValue( name );
		menuItem.setLink( url );
		return menuItem;
	}
}