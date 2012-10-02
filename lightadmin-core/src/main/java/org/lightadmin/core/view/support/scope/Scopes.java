package org.lightadmin.core.view.support.scope;

import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class Scopes implements Iterable<Scope> {

	private final List<Scope> scopes;

	public Scopes( final List<Scope> scopes ) {
		Assert.notNull( scopes );
		this.scopes = newLinkedList( scopes );
	}

	@Override
	public Iterator<Scope> iterator() {
		return scopes.iterator();
	}
}