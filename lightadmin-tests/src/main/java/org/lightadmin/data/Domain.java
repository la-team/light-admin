package org.lightadmin.data;

public enum Domain {

	PRODUCTS( "product", "Products" ),
	ORDERS( "order", "Orders" ),
	ADDRESSES( "address", "Addresses" ),
	CUSTOMERS( "customer", "Customers" );

	private final String domainTypeName;
	private final String linkText;

	Domain( String domainTypeName, String linkText ) {
		this.domainTypeName = domainTypeName;
		this.linkText = linkText;
	}

	public String getDomainTypeName() {
		return domainTypeName;
	}

	public String getLinkText() {
		return linkText;
	}
}
