package org.lightadmin.test.scope;

import org.lightadmin.api.config.utils.DomainTypeSpecification;
import org.lightadmin.test.model.TestCustomer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DummySpecification implements DomainTypeSpecification<TestCustomer> {

    @Override
    public Predicate toPredicate(Root<TestCustomer> testCustomerRoot, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(testCustomerRoot.get("firstname"), "Dave");
    }
}