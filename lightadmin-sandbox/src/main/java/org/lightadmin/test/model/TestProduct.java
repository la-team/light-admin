package org.lightadmin.test.model;

import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.math.BigDecimal;

@Entity
public class TestProduct extends AbstractEntity {

	private String name;

	private BigDecimal price;

	@Lob
	private byte[] picture;

	public TestProduct() {}


	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice( BigDecimal price ) {
		this.price = price;
	}
}
