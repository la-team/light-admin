package org.lightadmin.test.model;
import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class TestLineItem extends AbstractEntity {

	@ManyToOne
	@JoinColumn( name = "product_id" )
	private TestProduct product;

	private Integer amount;

	public TestLineItem() { }

	public TestProduct getProduct() {
		return product;
	}

	public Integer getAmount() {
		return amount;
	}

	public BigDecimal getPrice() {
		return product.getPrice();
	}

	public BigDecimal getTotal() {
		return getPrice().multiply( BigDecimal.valueOf( amount ) );
	}
}
