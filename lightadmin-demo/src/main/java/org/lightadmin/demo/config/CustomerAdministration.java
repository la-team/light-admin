package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.util.Pair;
import org.lightadmin.demo.model.Customer;
import org.lightadmin.demo.model.Product;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@Administration( Customer.class )
public class CustomerAdministration {

	public static Set<Pair<String, String>> listColumns() {
		Set<Pair<String, String>> result = newLinkedHashSet();
		result.add( Pair.stringPair( "firstname", "First Name" ) );
		result.add( Pair.stringPair( "lastname", "Last Name" ) );
		result.add( Pair.stringPair( "emailAddress", "Email Address" ) );
		return result;
	}
}