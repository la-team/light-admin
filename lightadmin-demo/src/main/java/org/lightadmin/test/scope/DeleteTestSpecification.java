package org.lightadmin.test.scope;

import org.lightadmin.api.config.utils.DomainTypeSpecification;
import org.lightadmin.test.model.TestCustomer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DeleteTestSpecification implements DomainTypeSpecification<TestCustomer> {

    @Override
    public Predicate toPredicate(Root<TestCustomer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("firstname"), "Test");
    }
}
