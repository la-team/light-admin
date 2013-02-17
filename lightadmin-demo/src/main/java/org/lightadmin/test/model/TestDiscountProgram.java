package org.lightadmin.test.model;

import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class TestDiscountProgram  extends AbstractEntity {

	@NotNull
	private String name;

	@ManyToMany( mappedBy = "discountPrograms" )
	private Set<TestCustomer> customers;

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public Set<TestCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers( final Set<TestCustomer> testCustomers ) {
		this.customers = testCustomers;
	}
}
