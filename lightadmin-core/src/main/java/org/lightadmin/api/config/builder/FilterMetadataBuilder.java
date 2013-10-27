package org.lightadmin.api.config.builder;

import org.lightadmin.core.config.domain.filter.FilterMetadata;

public interface FilterMetadataBuilder {

    FilterMetadataBuilder field(String fieldName);

    FilterMetadataBuilder caption(String filterName);

    FilterMetadata build();

}