package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.util.Pair;
import org.lightadmin.demo.model.Product;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@Administration( Product.class )
public class ProductAdministration {

	public static Set<Pair<String, String>> listColumns() {
		Set<Pair<String, String>> result = newLinkedHashSet();
		result.add( Pair.stringPair( "name", "Name" ) );
		result.add( Pair.stringPair( "description", "Description" ) );
		result.add( Pair.stringPair( "price", "Price" ) );
		return result;
	}
}