package org.lightadmin.core.search;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ClassUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newLinkedList;
import static org.apache.commons.lang.BooleanUtils.toBoolean;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.math.NumberUtils.isNumber;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.*;
import static org.springframework.util.NumberUtils.parseNumber;

@SuppressWarnings( "unchecked" )
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
			public javax.persistence.criteria.Predicate toPredicate( final Root<Object> root, final CriteriaQuery<?> query, final CriteriaBuilder builder ) {
				final List<Predicate> attributesPredicates = newLinkedList();
				for ( String parameterName : parameters.keySet() ) {
					if ( domainTypeEntityMetadata.containsAttribute( parameterName ) ) {
						final DomainTypeAttributeMetadata attribute = domainTypeEntityMetadata.getAttribute( parameterName );
						final String attributeName = attribute.getName();

						final String[] parameterValues = parameters.get( attributeName );

						for ( String parameterValue : parameterValues ) {
							final boolean numericHack = "0.00".equals( parameterValue ); //TODO: max: hack for skipping numeric filters initialized by default

							if ( isBlank( parameterValue ) || numericHack ) {
								continue;
							}

							final Predicate attributePredicate = attributePredicate( root, builder, attribute, attributeName, parameterValue, domainTypeEntityMetadata );

							attributesPredicates.add( attributePredicate );
						}
					}
				}

				return builder.and( attributesPredicates.toArray( new Predicate[attributesPredicates.size()] ) );
			}
		};
	}

	private Predicate attributePredicate( final Root<Object> root, final CriteriaBuilder builder, final DomainTypeAttributeMetadata attribute, final String attributeName, final String parameterValue, final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata ) {
		if ( isNumericType( attribute ) && isNumber( parameterValue ) ) {
			final Number attributeValue = parseNumber( parameterValue, ( Class<? extends Number> ) attribute.getType() );

			return numericAttributePredicate( root, builder, attributeName, attributeValue );
		}

		if ( isBooleanType( attribute ) ) {
			return booleanAttributePredicate( root, builder, attributeName, parameterValue );
		}

		if ( isAssociation( attribute ) ) {
			return associationAttributePredicate( root, builder, attribute, attributeName, parameterValue, domainTypeEntityMetadata );
		}

		return stringAttributePredicate( root, builder, attributeName, parameterValue );
	}

	private Predicate stringAttributePredicate( final Root<Object> root, final CriteriaBuilder builder, final String attributeName, final String parameterValue ) {
		return builder.like( root.<String>get( attributeName ), "%" + parameterValue + "%" );
	}

	private Predicate associationAttributePredicate( final Root<Object> root, final CriteriaBuilder builder, final DomainTypeAttributeMetadata attribute, final String attributeName, final String parameterValue, final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata ) {
		final Class<?> domainType = attribute.getAssociationDomainType();

		final DynamicJpaRepository repository = domainTypeConfigurationFor( domainType ).getRepository();

		Serializable id = stringToSerializable( parameterValue, ( Class<? extends Serializable> ) domainTypeEntityMetadata.getIdAttribute().getType() );

		final Object entity = repository.findOne( id );

		if ( attribute.isCollectionLike() ) {
			final Expression<Collection> objectPath = root.get( attributeName );
			return builder.isMember( entity, objectPath );
		} else {
			return builder.equal( root.<String>get( attributeName ), entity );
		}
	}

	private Predicate booleanAttributePredicate( final Root<Object> root, final CriteriaBuilder builder, final String attributeName, final String parameterValue ) {
		final Boolean boolValue = toBoolean( parameterValue );

		return builder.equal( root.<String>get( attributeName ), boolValue );
	}

	private Predicate numericAttributePredicate( final Root<Object> root, final CriteriaBuilder builder, final String attributeName, final Number attributeValue ) {
		return builder.equal( root.<String>get( attributeName ), attributeValue );
	}

	private boolean isAssociation( final DomainTypeAttributeMetadata attribute ) {
		final DomainTypeAttributeType domainTypeAttributeType = DomainTypeAttributeType.by( attribute );

		return domainTypeAttributeType == ASSOC || domainTypeAttributeType == ASSOC_MULTI;
	}

	private boolean isBooleanType( final DomainTypeAttributeMetadata attribute ) {
		return DomainTypeAttributeType.by( attribute ) == BOOL;
	}

	private boolean isNumericType( final DomainTypeAttributeMetadata attribute ) {
		return DomainTypeAttributeType.by( attribute ) == NUMBER;
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