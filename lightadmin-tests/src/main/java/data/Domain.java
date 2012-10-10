package data;

public enum Domain {
    PRODUCTS( "Products" ),
    ORDERS( "Orders" ),
    ADDRESSES( "Addresses" ),
    CUSTOMERS( "Customers" );

    private String linkText;

    Domain( String linkText ) {
        this.linkText = linkText;
    }

    public String getLinkText() {
        return linkText;
    }
}
