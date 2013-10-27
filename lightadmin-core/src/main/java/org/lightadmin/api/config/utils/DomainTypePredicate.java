package org.lightadmin.api.config.utils;

import com.google.common.base.Predicate;

import java.io.Serializable;

public interface DomainTypePredicate<T> extends Predicate<T>, Serializable {

}