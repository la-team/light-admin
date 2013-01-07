package org.lightadmin.test.scope;

import org.lightadmin.test.model.TestCustomer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DummySpecification implements Specification<TestCustomer> {

	@Override
	public Predicate toPredicate( Root<TestCustomer> testCustomerRoot, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder ) {
		return criteriaBuilder.equal( testCustomerRoot.get( "firstname" ), "Dave" );
	}
}
