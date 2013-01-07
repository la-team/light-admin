package org.lightadmin.core.config.domain.scope;

import com.google.common.base.Predicate;

import java.io.Serializable;

public interface DomainTypePredicate<T> extends Predicate<T>, Serializable {

}