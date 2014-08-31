package org.lightadmin.data;

public enum Domain {

	TEST_CUSTOMERS( "testCustomer", "Test Customers Domain", 100 ),
	FILTER_TEST_DOMAIN( "filterTestEntity", "FilterTest Domain", 100 ),
	TEST_ORDERS( "testOrder", "Test Order Domain", 100 ),
	TEST_PRODUCTS( "testProduct", "Test Products", 100 ),
	ENTITIES_WITH_UUIDS( "entityWithUuid", "EntityWithUuid Domain", 100 );

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

    public Domain setExpectedRecordCount(int expectedRecordCount) {
        this.expectedRecordsCount = expectedRecordCount;
		return this;
    }

    public int getExpectedRecordsCount() {
        return expectedRecordsCount;
    }

}
