package org.lightadmin.core.config.domain.unit.handler;

import org.lightadmin.core.config.domain.filter.FilterMetadata;

public interface FilterHandler<T extends FilterMetadata> {

    void doWithFilter(T filter);
}