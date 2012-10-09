package org.lightadmin.core.search;

import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newLinkedList;

public class SpecificationCreator {

	public Specification toSpecification( DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata, final Map<String, String[]> parameters ) {
		final Collection<? extends DomainTypeAttributeMetadata> attributes = domainTypeEntityMetadata.getAttributes();

		return new Specification<Object>() {
			@Override
			public javax.persistence.criteria.Predicate toPredicate( final Root<Object> root, final CriteriaQuery<?> query, final CriteriaBuilder builder ) {
				final List<Predicate> attributesPredicates = newLinkedList();
				for ( DomainTypeAttributeMetadata attribute : attributes ) {
					final String attributeName = attribute.getName();
					if ( parameters.containsKey( attributeName ) ) {
						final String[] parameterValues = parameters.get( attributeName );
						final String parameterValue = parameterValues[0];
						if ( StringUtils.isBlank( parameterValue ) ) {
							continue;
						}

						javax.persistence.criteria.Predicate attributePredicate = builder.like( root.<String>get( attributeName ), "%" + parameterValue + "%" );

						attributesPredicates.add( attributePredicate );
					}
				}

				final javax.persistence.criteria.Predicate[] predicates = attributesPredicates.toArray( new javax.persistence.criteria.Predicate[attributesPredicates.size()] );

				query.where( builder.and( predicates ) );

				return null;
			}
		};
	}
}