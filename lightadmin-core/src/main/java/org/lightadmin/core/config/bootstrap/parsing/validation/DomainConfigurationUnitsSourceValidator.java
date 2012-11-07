package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.InvalidPropertyConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.config.domain.fragment.FieldMetadata;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Set;

class DomainConfigurationUnitsSourceValidator implements DomainConfigurationSourceValidator<DomainConfigurationSource> {

	private DomainTypePropertyValidator domainTypePropertyValidator;

	public DomainConfigurationUnitsSourceValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.domainTypePropertyValidator = new SimpleDomainTypePropertyValidator( entityMetadataResolver );
	}

	@Override
	public void validate( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		validateFilters( domainConfigurationSource, problemReporter );

		validateListView( domainConfigurationSource, problemReporter );
	}

	void validateListView( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		final Fragment listViewFragment = domainConfigurationSource.getListViewFragment().getFragment();
		Set<FieldMetadata> fields = listViewFragment.getFields();

		for ( FieldMetadata field : fields ) {
			if ( domainTypePropertyValidator.isInvalidProperty( field.getFieldName(), domainConfigurationSource.getDomainTypeEntityMetadata() ) ) {
				problemReporter.error( new InvalidPropertyConfigurationProblem( field.getFieldName(), domainConfigurationSource, DomainConfigurationUnitType.LIST_VIEW ) );
			}
		}
	}

	void validateFilters( final DomainConfigurationSource domainConfigurationSource, final ProblemReporter problemReporter ) {
		for ( FilterMetadata filter : domainConfigurationSource.getFilters() ) {
			final String fieldName = filter.getFieldName();
			if ( domainTypePropertyValidator.isInvalidProperty( fieldName, domainConfigurationSource.getDomainTypeEntityMetadata() ) ) {
				problemReporter.warning( new InvalidPropertyConfigurationProblem( fieldName, domainConfigurationSource, DomainConfigurationUnitType.FILTERS ) );
			}
		}
	}
}