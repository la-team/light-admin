package radmin;

import java.math.BigDecimal;

public class Entity {

	private int id;
	private String name;
	private BigDecimal price;
	private boolean published;

	public Entity() {
	}

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice( final BigDecimal price ) {
		this.price = price;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished( final boolean published ) {
		this.published = published;
	}
}