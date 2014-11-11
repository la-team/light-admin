package org.lightadmin.demo.model;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "Orders")
public class Order extends AbstractEntity {

	@ManyToOne
	@NotNull
	private Customer customer;

	@ManyToOne
	private Address billingAddress;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@NotNull
	private Address shippingAddress;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private Set<LineItem> lineItems;

	/**
	 * Creates a new {@link Order} for the given {@link Customer}.
	 *
	 * @param customer        must not be {@literal null}.
	 * @param shippingAddress must not be {@literal null}.
	 */
	public Order( Customer customer, Address shippingAddress ) {
		this( customer, shippingAddress, null );
	}

	/**
	 * Creates a new {@link Order} for the given customer, shipping and billing {@link Address}.
	 *
	 * @param customer        must not be {@literal null}.
	 * @param shippingAddress must not be {@literal null}.
	 * @param billingAddress  can be {@@iteral null}.
	 */
	public Order( Customer customer, Address shippingAddress, Address billingAddress ) {
		Assert.notNull( customer );
		Assert.notNull( shippingAddress );

		this.customer = customer;
		this.shippingAddress = shippingAddress.getCopy();
		this.billingAddress = billingAddress == null ? null : billingAddress.getCopy();
	}

	public Order() {

	}

	/**
	 * Adds the given {@link LineItem} to the {@link Order}.
	 *
	 * @param lineItem
	 */
	public void add( LineItem lineItem ) {
		this.lineItems.add( lineItem );
	}

	/**
	 * Returns the {@link Customer} who placed the {@link Order}.
	 *
	 * @return
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Returns the billing {@link Address} for this order.
	 *
	 * @return
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * Returns the shipping {@link Address} for this order;
	 *
	 * @return
	 */
	public Address getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * Returns all {@link LineItem}s currently belonging to the {@link Order}.
	 *
	 * @return
	 */
	public Set<LineItem> getLineItems() {
		return lineItems;
	}

	/**
	 * Returns the total of the {@link Order}.
	 *
	 * @return
	 */
	public BigDecimal getTotal() {

		BigDecimal total = BigDecimal.ZERO;

		for ( LineItem item : lineItems ) {
			total = total.add( item.getTotal() );
		}

		return total;
	}
}
