package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.util.Pair;
import org.lightadmin.demo.model.Customer;
import org.lightadmin.demo.model.Order;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

//@Administration( Order.class )
public class OrderAdministration {

	public static Set<Pair<String, String>> listColumns() {
		Set<Pair<String, String>> result = newLinkedHashSet();
		result.add( Pair.stringPair( "customer.firstname", "Customer" ) );
		return result;
	}
}