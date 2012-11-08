package org.lightadmin.demo.model;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class FilterTestEntity extends AbstractEntity {

	private String textfield;

	private BigDecimal integerfield;

	private BigDecimal decimalfield;

	public FilterTestEntity( String textfield, BigDecimal integer, BigDecimal numberfield ) {
		this.textfield = textfield;
		this.integerfield = integer;
		this.decimalfield = numberfield;
	}

	protected FilterTestEntity() {

	}

}
