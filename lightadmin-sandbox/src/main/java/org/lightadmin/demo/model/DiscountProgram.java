package org.lightadmin.demo.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class DiscountProgram extends AbstractEntity {

	@NotNull
	@Size( min = 10, max = 100 )
	private String name;

	@ManyToMany( mappedBy = "discountPrograms" )
	private Set<Customer> customers;

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers( final Set<Customer> customers ) {
		this.customers = customers;
	}
}