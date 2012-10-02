package org.lightadmin.core.view.preparer;

import com.google.common.base.Function;
import org.apache.tiles.beans.MenuItem;
import org.apache.tiles.beans.SimpleMenuItem;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;

public class DomainConfigToMenuItemTransformation implements Function<DomainTypeAdministrationConfiguration, MenuItem> {

	public static final DomainConfigToMenuItemTransformation INSTANCE = new DomainConfigToMenuItemTransformation();

	private DomainConfigToMenuItemTransformation() {
	}

	@Override
	public MenuItem apply( final DomainTypeAdministrationConfiguration domainConfiguration ) {
		return menuItem( domainConfiguration.getDomainTypeName(), resolveDomainContextUrl( domainConfiguration ) );
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