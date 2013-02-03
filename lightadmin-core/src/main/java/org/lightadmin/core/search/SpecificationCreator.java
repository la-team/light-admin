package org.lightadmin.core.search;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newLinkedList;
import static org.springframework.util.ClassUtils.isAssignable;

public class SpecificationCreator {

	private final GlobalAdministrationConfiguration configuration;

	public SpecificationCreator( final GlobalAdministrationConfiguration configuration ) {
		this.configuration = configuration;
	}

	public Specification toSpecification( final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata, final Map<String, String[]> parameters ) {
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

						javax.persistence.criteria.Predicate attributePredicate;
						if ( isAssignable( Number.class, attribute.getType() ) && NumberUtils.isNumber( parameterValue ) ) {
							attributePredicate = builder.equal( root.<String>get( attributeName ), org.springframework.util.NumberUtils.parseNumber( parameterValue, ( Class<? extends Number> ) attribute.getType() ) );
						} else if (Boolean.class.equals( attribute.getType() ) || boolean.class.equals( attribute.getType() )) {
							final Boolean boolValue = BooleanUtils.toBoolean( parameterValue );

							attributePredicate = builder.equal( root.<String>get( attributeName ), boolValue );
						} else if ( attribute.isAssociation() ) {
							final Class<?> domainType = attribute.getAssociationDomainType();

							final DynamicJpaRepository repository = domainTypeConfigurationFor( domainType ).getRepository();

							Serializable id = parseIdNumber( parameterValue, domainTypeEntityMetadata );

							attributePredicate = builder.equal( root.<String>get( attributeName ), repository.findOne( id ) );
						} else {
							attributePredicate = builder.like( root.<String>get( attributeName ), "%" + parameterValue + "%" );
						}

						attributesPredicates.add( attributePredicate );
					}
				}

				final javax.persistence.criteria.Predicate[] predicates = attributesPredicates.toArray( new javax.persistence.criteria.Predicate[attributesPredicates.size()] );

				return builder.and( predicates );
			}
		};
	}

	private Number parseIdNumber( final String parameterValue, final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata ) {
		return org.springframework.util.NumberUtils.parseNumber( parameterValue, ( Class<? extends Number> ) domainTypeEntityMetadata.getIdAttribute().getType() );
	}

	private DomainTypeBasicConfiguration domainTypeConfigurationFor( final Class<?> domainType ) {
		final DomainTypeBasicConfiguration domainTypeBasicConfiguration = configuration.forDomainType( domainType );
		if ( domainTypeBasicConfiguration != null ) {
			return domainTypeBasicConfiguration;
		}

		return configuration.forManagedDomainType( domainType );
	}
}