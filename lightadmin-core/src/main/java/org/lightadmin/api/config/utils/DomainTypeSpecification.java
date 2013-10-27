package org.lightadmin.api.config.utils;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

public interface DomainTypeSpecification<T> extends Specification<T>, Serializable {

}