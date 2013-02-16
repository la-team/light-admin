package org.lightadmin.core.persistence.repository;

public interface NullPlaceholderSupport<T> {

	T getNullPlaceholder();

	boolean isNullPlaceholder(Object val);

}
