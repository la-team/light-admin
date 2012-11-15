package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.InvalidPropertyConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;
import org.springframework.util.ClassUtils;

import java.util.Set;

class DomainConfigurationUnitsSourceValidator implements DomainConfigurationSourceValidator<DomainConfigurationSource> {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	private final FieldMetadataValidator<FieldMetadata> fieldMetadataValidator;

	public DomainConfigurationUnitsSourceValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;

		this.fieldMetadataValidator = new DomainTypeFieldMetadataValidator();
	}

	@Override
	public void validate( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		validateDomainType( domainConfigurationSource, problemReporter );

		validateFilters( domainConfigurationSource, problemReporter );

		validateListView( domainConfigurationSource, problemReporter );
	}

	private void validateDomainType( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final Class<?> domainType = domainConfigurationSource.getDomainType();

		if ( !isPersistentEntityType( domainType ) ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfigurationSource, String.format( "Non-persistent type %s is not supported.", domainType.getSimpleName() ) ) );
		}

		if ( !ClassUtils.hasConstructor( domainType ) ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfigurationSource, String.format( "Type %s must have default constructor.", domainType.getSimpleName() ) ) );
		}
	}

	void validateListView( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final Fragment listViewFragment = domainConfigurationSource.getListViewFragment().getFragment();

		validateFields( listViewFragment.getFields(), domainConfigurationSource, DomainConfigurationUnitType.LIST_VIEW, problemReporter );
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

	private boolean isPersistentEntityType( final Class<?> domainType ) {
		return entityMetadataResolver.resolveEntityMetadata( domainType ) != null;
	}
}