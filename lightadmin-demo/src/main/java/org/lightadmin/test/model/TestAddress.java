package org.lightadmin.test.model;

import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import java.util.Set;

@Entity
public class TestAddress extends AbstractEntity {

	private String street, city, country;

	@ManyToMany( mappedBy = "shippingAddresses" )
	private Set<TestOrder> orders;

	public TestAddress () {}


	public String getStreet() {
		return street;
	}

	public void setStreet( String street ) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity( String city ) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry( String country ) {
		this.country = country;
	}

	public Set<TestOrder> getOrders() {
		return orders;
	}

	public void setOrders( Set<TestOrder> orders ) {
		this.orders = orders;
	}
}

