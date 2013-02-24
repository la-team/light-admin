package org.lightadmin.demo.model;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.util.Assert;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Entity
public class Product extends AbstractEntity {

	@NotNull
	@Size( min = 10, max = 100 )
	private String name;

	private String description;

	@NotNull
	@NumberFormat(style = NumberFormat.Style.CURRENCY)
	private BigDecimal price;

	@ElementCollection
	private Map<String, String> attributes;

	@Basic
	private Boolean retired;

	@Temporal(TemporalType.DATE) @Column(name = "REL_DATE")
	private Date releaseDate;

	public Product( String name, BigDecimal price ) {
		this( name, price, null );
	}

	public Product( String name, BigDecimal price, String description ) {
		Assert.hasText( name, "Name must not be null or empty!" );
		Assert.isTrue( BigDecimal.ZERO.compareTo( price ) < 0, "Price must be greater than zero!" );

		this.name = name;
		this.price = price;
		this.description = description;
	}

	public Product() {
	}

	public void setAttribute( String name, String value ) {
		Assert.hasText( name );

		if ( value == null ) {
			this.attributes.remove( value );
		} else {
			this.attributes.put( name, value );
		}
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Boolean getRetired() {
		return retired;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

}
