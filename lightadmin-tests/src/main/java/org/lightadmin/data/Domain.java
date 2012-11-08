package org.lightadmin.data;

public enum Domain {

	PRODUCTS( "product", "Products", 100 ),
	ORDERS( "order", "Orders", 100 ),
	ADDRESSES( "address", "Addresses", 100 ),
	CUSTOMERS( "customer", "Customers", 100 ),
	TEST_DOMAIN( "filterTestEntity", "FilterTest Domain", 100 );

	private final String domainTypeName;
	private final String linkText;
    private int expectedRecordsCount;
    private int domainRecordsCapacity;

    Domain(String domainTypeName, String linkText, int domainRecordsCapacity) {
		this.domainTypeName = domainTypeName;
		this.linkText = linkText;
        this.domainRecordsCapacity = domainRecordsCapacity;
    }

	public String getDomainTypeName() {
		return domainTypeName;
	}

	public String getLinkText() {
		return linkText;
	}

    public void setExpectedRecordCount(int expectedRecordCount) {
        this.expectedRecordsCount = expectedRecordCount;
    }

    public int getExpectedRecordsCount() {
        return expectedRecordsCount;
    }

    public int getExpectedRecordsPercentage() {
        return 100 * expectedRecordsCount / domainRecordsCapacity;
    }
}
