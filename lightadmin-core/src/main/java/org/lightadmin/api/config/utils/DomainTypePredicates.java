package org.lightadmin.api.config.utils;

public class DomainTypePredicates {

    private DomainTypePredicates() {
    }

    public static <T> DomainTypePredicate<T> alwaysTrue() {
        return new DomainTypePredicate<T>() {
            @Override
            public boolean apply(final Object input) {
                return true;
            }
        };
    }
}