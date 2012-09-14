package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.util.Pair;
import org.lightadmin.demo.model.Address;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@Administration( Address.class )
public class AddressAdministration {

	public static Set<Pair<String, String>> listColumns() {
		Set<Pair<String, String>> result = newLinkedHashSet();
		result.add( Pair.stringPair( "country", "Country" ) );
		result.add( Pair.stringPair( "city", "City" ) );
		result.add( Pair.stringPair( "street", "Street" ) );
		return result;
	}

}