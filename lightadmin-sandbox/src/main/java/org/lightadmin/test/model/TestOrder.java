package org.lightadmin.test.model;

import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@Entity
@Table(name = "TestOrders")
public class TestOrder extends AbstractEntity {

	private String name;

	@ManyToOne(cascade = CascadeType.ALL)
	private TestCustomer customer;

	@ManyToMany
	@JoinTable(name = "testorders_addresses",
			joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "ID"),
			inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "ID")
	)
	private Set<TestAddress> shippingAddresses;

	@Temporal(TemporalType.DATE)
	private Date dueDate;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<TestLineItem> lineItems = newArrayList();

	@Transient
	private BigDecimal orderTotal;

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

    public void setLineItems(List<TestLineItem> lineItems) {
        this.lineItems.clear();
        this.lineItems.addAll(lineItems);
    }

    public List<TestLineItem> getLineItems() {
		return lineItems;
	}

	public BigDecimal getOrderTotal() {
		BigDecimal orderTotal = new BigDecimal( 0 );

		for ( TestLineItem lineItem : lineItems ) {
			orderTotal = orderTotal.add( lineItem.getTotal() );
		}

		return orderTotal;
	}

	public TestCustomer getCustomer() {
		return customer;
	}

	public void setCustomer( TestCustomer customer ) {
		this.customer = customer;
	}


	public Set<TestAddress> getShippingAddresses() {
		return shippingAddresses;
	}

	public void setShippingAddresses( final Set<TestAddress> shippingAddresses ) {
		this.shippingAddresses = shippingAddresses;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate( Date dueDate ) {
		this.dueDate = dueDate;
	}
}