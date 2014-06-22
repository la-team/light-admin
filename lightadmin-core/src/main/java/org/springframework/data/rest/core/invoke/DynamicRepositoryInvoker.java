package org.springframework.data.rest.core.invoke;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface DynamicRepositoryInvoker extends RepositoryInvoker {

    Page findAll(Specification spec, Pageable pageable);

    List findAll(Specification spec, Sort sort);

    List findAll(Specification spec);

    long count(Specification spec);
}