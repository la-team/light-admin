package org.lightadmin.test.model;

import org.lightadmin.demo.model.AbstractEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class FilterTestEntity extends AbstractEntity {

	private String textField;

	private Integer integerField;

	private int primitiveIntegerField;

	private BigDecimal decimalField;

	private BigDecimal calculatedField;

	private Boolean booleanField;

    public FilterTestEntity(final String textField, final int integerField, final BigDecimal decimalField, Boolean booleanField) {
        this.textField = textField;
		this.integerField = integerField;
		this.decimalField = decimalField;
		this.booleanField = booleanField;
	}

	public FilterTestEntity() {
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField( final String textField ) {
		this.textField = textField;
	}

	public Integer getIntegerField() {
		return integerField;
	}

	public void setIntegerField( final Integer integerField ) {
		this.integerField = integerField;
	}

	public BigDecimal getDecimalField() {
		return decimalField;
	}

	public void setDecimalField( final BigDecimal decimalField ) {
		this.decimalField = decimalField;
	}

	public int getPrimitiveIntegerField() {
		return primitiveIntegerField;
	}

	public void setPrimitiveIntegerField( int primitiveIntegerField ) {
		this.primitiveIntegerField = primitiveIntegerField;
	}

	public BigDecimal getCalculatedField() {
		BigDecimal theResult = new BigDecimal( 0 );
		return theResult.add( decimalField ).add( new BigDecimal( integerField ) ).add( new BigDecimal( primitiveIntegerField ) );
	}

    public Boolean isBooleanField() {
        return booleanField;
	}

    public void setBooleanField(Boolean booleanField) {
        this.booleanField = booleanField;
	}
}