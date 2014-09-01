package org.lightadmin.demo.model;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class LineItem extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	@NotNull
	@NumberFormat(style = NumberFormat.Style.CURRENCY)
	private BigDecimal price;

	private Integer amount;

	/**
	 * Creates a new {@link LineItem} for the given {@link Product}.
	 *
	 * @param product must not be {@literal null}.
	 */
	public LineItem( Product product ) {
		this( product, 1 );
	}

	/**
	 * Creates a new {@link LineItem} for the given {@link Product} and amount.
	 *
	 * @param product must not be {@literal null}.
	 * @param amount
	 */
	public LineItem( Product product, int amount ) {

		Assert.notNull( product, "The given Product must not be null!" );
		Assert.isTrue( amount > 0, "The amount of Products to be bought must be greater than 0!" );

		this.product = product;
		this.amount = amount;
		this.price = product.getPrice();
	}

	public LineItem() {

	}

	/**
	 * Returns the {@link Product} the {@link LineItem} refers to.
	 *
	 * @return
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Returns the amount of {@link Product}s to be ordered.
	 *
	 * @return
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * Returns the price a single unit of the {@link LineItem}'s product.
	 *
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Returns the total for the {@link LineItem}.
	 *
	 * @return
	 */
	public BigDecimal getTotal() {
		return price.multiply( BigDecimal.valueOf( amount ) );
	}
}