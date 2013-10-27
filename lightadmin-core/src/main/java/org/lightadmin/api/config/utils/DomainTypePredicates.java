package org.lightadmin.api.config.utils;

import javax.annotation.Nullable;

public class DomainTypePredicates {

    private DomainTypePredicates() {
    }

    public static <T> DomainTypePredicate<T> alwaysTrue() {
        return new DomainTypePredicate<T>() {
            @Override
            public boolean apply(@Nullable final Object input) {
                return true;
            }
        };
    }
}