package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.InvalidPropertyConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Set;

import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.SCOPES;
import static org.lightadmin.core.config.domain.scope.ScopeMetadataUtils.*;
import static org.springframework.util.ClassUtils.hasConstructor;

class DomainConfigurationUnitsSourceValidator implements DomainConfigurationSourceValidator<DomainConfigurationSource> {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	private final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator;

	public DomainConfigurationUnitsSourceValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this( entityMetadataResolver, new DomainTypeFieldMetadataValidator( entityMetadataResolver ) );
	}

	public DomainConfigurationUnitsSourceValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver, final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator ) {
		this.entityMetadataResolver = entityMetadataResolver;
		this.fieldMetadataValidator = fieldMetadataValidator;
	}

	@Override
	public void validate( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		validateDomainType( domainConfigurationSource, problemReporter );

		validateFilters( domainConfigurationSource, problemReporter );

		validateListView( domainConfigurationSource, problemReporter );

		validateShowView( domainConfigurationSource, problemReporter );

		validateQuickView( domainConfigurationSource, problemReporter );

		validateFormView( domainConfigurationSource, problemReporter );

		validateScopes( domainConfigurationSource, problemReporter );
	}

	void validateDomainType( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final Class<?> domainType = domainConfigurationSource.getDomainType();

		if ( !isPersistentEntityType( domainType ) ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfigurationSource, String.format( "Non-persistent type %s is not supported.", domainType.getSimpleName() ) ) );
		}

		if ( !hasConstructor( domainType ) ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfigurationSource, String.format( "Type %s must have default constructor.", domainType.getSimpleName() ) ) );
		}
	}

	void validateScopes( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final ScopesConfigurationUnit scopes = domainConfigurationSource.getScopes();

		for ( ScopeMetadata scope : scopes ) {
			if ( isPredicateScope( scope ) ) {
				validatePredicateScope( scope, domainConfigurationSource, problemReporter );
			}

			if ( isSpecificationScope( scope ) ) {
				validateSpecificationScope( scope, domainConfigurationSource, problemReporter );
			}
		}
	}

	void validateSpecificationScope( final ScopeMetadata scope, final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		SpecificationScopeMetadata specificationScopeMetadata = ( SpecificationScopeMetadata ) scope;
		if ( specificationScopeMetadata.specification() == null ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfigurationSource, SCOPES, "Filtering specification not defined for scope " + scope.getName() ) );
		}
	}

	void validatePredicateScope( final ScopeMetadata scope, final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		PredicateScopeMetadata predicateScopeMetadata = ( PredicateScopeMetadata ) scope;
		if ( predicateScopeMetadata.predicate() == null ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfigurationSource, SCOPES, "Filtering predicate not defined for scope " + scope.getName() ) );
		}
	}

	void validateFormView( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final FieldSetConfigurationUnit formViewFieldSet = domainConfigurationSource.getFormViewFragment();

		validateFields( formViewFieldSet.getFields(), domainConfigurationSource, formViewFieldSet.getDomainConfigurationUnitType(), problemReporter );
	}

	void validateQuickView( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final FieldSetConfigurationUnit quickViewFieldSet = domainConfigurationSource.getQuickViewFragment();

		validateFields( quickViewFieldSet.getFields(), domainConfigurationSource, quickViewFieldSet.getDomainConfigurationUnitType(), problemReporter );
	}

	void validateShowView( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final FieldSetConfigurationUnit showViewFieldSet = domainConfigurationSource.getShowViewFragment();

		validateFields( showViewFieldSet.getFields(), domainConfigurationSource, showViewFieldSet.getDomainConfigurationUnitType(), problemReporter );
	}

	void validateListView( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final FieldSetConfigurationUnit listViewFieldSet = domainConfigurationSource.getListViewFragment();

		validateFields( listViewFieldSet.getFields(), domainConfigurationSource, listViewFieldSet.getDomainConfigurationUnitType(), problemReporter );
	}

	void validateFilters( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final Set<FieldMetadata> fields = FieldMetadataUtils.extractFields( domainConfigurationSource.getFilters() );

		validateFields( fields, domainConfigurationSource, DomainConfigurationUnitType.FILTERS, problemReporter );
	}

	private void validateFields( Set<FieldMetadata> fields, DomainConfigurationSource domainConfigurationSource, final DomainConfigurationUnitType configurationUnitType, ProblemReporter problemReporter ) {
		for ( FieldMetadata field : fields ) {
			if ( !fieldMetadataValidator.isValidFieldMetadata( field, domainConfigurationSource.getDomainType() ) ) {
				problemReporter.error( new InvalidPropertyConfigurationProblem( field.getName(), domainConfigurationSource, configurationUnitType ) );
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean isPersistentEntityType( final Class<?> domainType ) {
		return entityMetadataResolver.resolveEntityMetadata( domainType ) != null;
	}
}