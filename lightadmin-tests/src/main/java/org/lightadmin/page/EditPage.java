package org.lightadmin.page;

import org.lightadmin.SeleniumContext;

public class EditPage extends ModificationPage<EditPage> {

	private int itemId;

	public EditPage( SeleniumContext seleniumContext, String domainName, int itemId ) {
		super( seleniumContext, domainName );

		this.itemId = itemId;
	}

	protected void load() {
		webDriver().get( baseUrl() + "/domain/" + getDomainName() + "/" + itemId + "/edit" );
	}

	@Override
	protected int getItemId() {
		return itemId;
	}
}