package org.lightadmin.core.search;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ClassUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newLinkedList;
import static org.apache.commons.lang.BooleanUtils.toBoolean;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.trim;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.*;
import static org.lightadmin.core.util.NumberUtils.isNumber;
import static org.lightadmin.core.util.NumberUtils.parseNumber;

@SuppressWarnings("unchecked")
public class SpecificationCreator {

	private final ConversionService conversionService;
	private final GlobalAdministrationConfiguration configuration;

	public SpecificationCreator( final ConversionService conversionService, final GlobalAdministrationConfiguration configuration ) {
		this.conversionService = conversionService;
		this.configuration = configuration;
	}

	public Specification toSpecification( final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata, final Map<String, String[]> parameters ) {
		return new Specification<Object>() {
			@Override
			public Predicate toPredicate( final Root<Object> root, final CriteriaQuery<?> query, final CriteriaBuilder builder ) {
				return new PredicatesBuilder( root, builder, domainTypeEntityMetadata ).build( parameters );
			}
		};
	}

	private class PredicatesBuilder {

		private final Root<Object> root;
		private final CriteriaBuilder builder;
		private final DomainTypeEntityMetadata domainTypeEntityMetadata;

		private PredicatesBuilder( final Root<Object> root, final CriteriaBuilder builder, final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
			this.root = root;
			this.builder = builder;
			this.domainTypeEntityMetadata = domainTypeEntityMetadata;
		}

		public javax.persistence.criteria.Predicate build( final Map<String, String[]> parameters ) {
			final List<Predicate> attributesPredicates = newLinkedList();

			for ( String parameterName : parameters.keySet() ) {
				if ( domainTypeEntityMetadata.containsAttribute( parameterName ) ) {
					final DomainTypeAttributeMetadata attribute = domainTypeEntityMetadata.getAttribute( parameterName );
					final String attributeName = attribute.getName();

					final String[] parameterValues = parameters.get( attributeName );

					attributesPredicates.add( attributePredicate( attribute, attributeName, parameterValues ) );
				}
			}

			return builder.and( attributesPredicates.toArray( new Predicate[attributesPredicates.size()] ) );
		}

		private Predicate attributePredicate( DomainTypeAttributeMetadata attribute, final String attributeName, final String... parameterValues ) {
			final String parameterValue = trim( parameterValues[0] );

			if ( isNumericType( attribute ) ) {
				return numericAttributePredicate( attribute, attributeName, parameterValue );
			}

			if ( isBooleanType( attribute ) ) {
				return booleanAttributePredicate( attributeName, parameterValues );
			}

			if ( isAssociation( attribute ) ) {
				return associationAttributesPredicate( attribute, attributeName, parameterValues );
			}

			if ( isDateType( attribute ) ) {
				return dateAttributePredicate( attributeName, parameterValue );
			}

			return stringAttributePredicate( attributeName, parameterValue );
		}

		private Predicate dateAttributePredicate( final String attributeName, final String parameterValue ) {
			try {
				final Date date = Date.valueOf( parameterValue );

				return builder.equal( root.<String>get( attributeName ), date );
			} catch ( IllegalArgumentException e ) {
				return builder.and();
			}
		}

		private Predicate stringAttributePredicate( final String attributeName, final String parameterValue ) {
			if ( isNotBlank( parameterValue ) ) {
				return builder.like( root.<String>get( attributeName ), "%" + parameterValue + "%" );
			}

			return builder.and();
		}

		private Predicate associationAttributesPredicate( final DomainTypeAttributeMetadata attribute, final String attributeName, final String... parameterValues ) {
			final List<Predicate> attributesPredicates = newLinkedList();

			for ( String parameterValue : parameterValues ) {
				if ( isNotBlank( parameterValue ) ) {
					attributesPredicates.add( associationAttributePredicate( attribute, attributeName, parameterValue ) );
				}
			}

			return builder.and( attributesPredicates.toArray( new Predicate[attributesPredicates.size()] ) );
		}

		private Predicate associationAttributePredicate( final DomainTypeAttributeMetadata attribute, final String attributeName, final String parameterValue ) {
			final Class<?> domainType = attribute.getAssociationDomainType();

			final DomainTypeBasicConfiguration domainTypeBasicConfiguration = domainTypeConfigurationFor( domainType );

			final DynamicJpaRepository repository = domainTypeBasicConfiguration.getRepository();

			final Serializable id = stringToSerializable( parameterValue, ( Class<? extends Serializable> ) domainTypeBasicConfiguration.getDomainTypeEntityMetadata().getIdAttribute().getType() );

			final Object entity = repository.findOne( id );

			if ( attribute.isCollectionLike() ) {
				final Expression<Collection> objectPath = root.get( attributeName );
				return builder.isMember( entity, objectPath );
			} else {
				return builder.equal( root.get( attributeName ), entity );
			}
		}

		private Predicate booleanAttributePredicate( final String attributeName, final String... parameterValues ) {
			final List<Predicate> attributesPredicates = newLinkedList();

			for ( String parameterValue : parameterValues ) {
				if ( isNotBlank( parameterValue ) ) {
					final boolean boolValue = toBoolean( parameterValue );

					attributesPredicates.add( builder.equal( root.<String>get( attributeName ), boolValue ) );
				}
			}

			return builder.or( attributesPredicates.toArray( new Predicate[attributesPredicates.size()] ) );
		}

		private Predicate numericAttributePredicate( final DomainTypeAttributeMetadata attribute, final String attributeName, final String parameterValue ) {
			if ( isNumber( parameterValue ) ) {
				final Number attributeValue = parseNumber( parameterValue, ( Class<? extends Number> ) attribute.getType() );

				return builder.equal( root.<String>get( attributeName ), attributeValue );
			}

			return builder.and();
		}

		private boolean isDateType( final DomainTypeAttributeMetadata attribute ) {
			return attribute.getAttributeType() == DATE;
		}

		private boolean isAssociation( final DomainTypeAttributeMetadata attribute ) {
			return attribute.getAttributeType() == ASSOC || attribute.getAttributeType() == ASSOC_MULTI;
		}

		private boolean isBooleanType( final DomainTypeAttributeMetadata attribute ) {
			return attribute.getAttributeType() == BOOL;
		}

		private boolean isNumericType( final DomainTypeAttributeMetadata attribute ) {
			return attribute.getAttributeType() == NUMBER_INTEGER || attribute.getAttributeType() == NUMBER_FLOAT;
		}

		private DomainTypeBasicConfiguration domainTypeConfigurationFor( final Class<?> domainType ) {
			final DomainTypeBasicConfiguration domainTypeBasicConfiguration = configuration.forDomainType( domainType );
			if ( domainTypeBasicConfiguration != null ) {
				return domainTypeBasicConfiguration;
			}

			return configuration.forManagedDomainType( domainType );
		}

		private <V extends Serializable> V stringToSerializable( String s, Class<V> targetType ) {
			if ( ClassUtils.isAssignable( targetType, String.class ) ) {
				return ( V ) s;
			}
			return conversionService.convert( s, targetType );
		}
	}
}