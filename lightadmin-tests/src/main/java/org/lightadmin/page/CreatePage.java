package org.lightadmin.page;

import org.lightadmin.SeleniumContext;

public class CreatePage extends ModificationPage<CreatePage> {

	protected CreatePage( SeleniumContext seleniumContext, String domainName ) {
		super( seleniumContext, domainName );
	}

	@Override
	//todo: ikostenko: get id for item to be created
	protected int getItemId() {
		return 0;
	}

	protected void load() {
		webDriver().get( baseUrl() + "/domain/" + getDomainName() + "/create" );
	}

}

