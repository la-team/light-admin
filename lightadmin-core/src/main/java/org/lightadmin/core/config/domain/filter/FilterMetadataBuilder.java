package org.lightadmin.core.config.domain.filter;

public class FilterMetadataBuilder {

    private String filterName = "Undefined";
    private String fieldName = "Undefined";

    public static FilterMetadataBuilder filter() {
        return new FilterMetadataBuilder();
    }

    public FilterMetadataBuilder field(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FilterMetadataBuilder caption(String filterName) {
        this.filterName = filterName;
        return this;
    }

    public FilterMetadata build() {
        return FilterMetadataUtils.filter(filterName, fieldName);
    }
}