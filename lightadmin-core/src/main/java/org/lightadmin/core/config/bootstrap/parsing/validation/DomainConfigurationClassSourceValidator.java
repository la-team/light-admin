package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.InvalidPropertyConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.filter.Filter;
import org.lightadmin.core.config.domain.filter.FilterBuilder;
import org.lightadmin.core.config.domain.fragment.FieldMetadata;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Set;

import static org.lightadmin.core.util.DomainConfigurationUtils.isNotConfigurationUnitDefined;

public class DomainConfigurationClassSourceValidator implements DomainConfigurationSourceValidator<DomainConfigurationSource<Class>> {

	private DomainTypePropertyValidator domainTypePropertyValidator;

	DomainConfigurationClassSourceValidator( final DomainTypePropertyValidator domainTypePropertyValidator ) {
		this.domainTypePropertyValidator = domainTypePropertyValidator;
	}

	public DomainConfigurationClassSourceValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.domainTypePropertyValidator = new SimpleDomainTypePropertyValidator( entityMetadataResolver );
	}

	@Override
	public void validate( final DomainConfigurationSource<Class> domainConfigurationSource, final ProblemReporter problemReporter ) {
		validateScreenContext( domainConfigurationSource, problemReporter );

		validateEntityConfiguration( domainConfigurationSource, problemReporter );

		validateFilters( domainConfigurationSource, problemReporter );

		validateListView( domainConfigurationSource, problemReporter );
	}

	void validateEntityConfiguration( final DomainConfigurationSource<Class> domainConfigurationSource, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfigurationSource.getSource(), DomainConfigurationUnitType.CONFIGURATION, EntityConfigurationBuilder.class ) ) {
			problemReporter.warning( new DomainConfigurationProblem( domainConfigurationSource, DomainConfigurationUnitType.CONFIGURATION, "It's better to define \"configuration\" unit or system will use DomainTypeClass#getSimpleName for entity string representation!" ) );
		}
	}

	void validateScreenContext( final DomainConfigurationSource<Class> domainConfigurationSource, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfigurationSource.getSource(), DomainConfigurationUnitType.SCREEN_CONTEXT, ScreenContextBuilder.class ) ) {
			problemReporter.fatal( new DomainConfigurationProblem( domainConfigurationSource, DomainConfigurationUnitType.SCREEN_CONTEXT, "You need to define \"screenContext\" configuration unit!" ) );
		}
	}

	void validateListView( final DomainConfigurationSource<Class> domainConfigurationSource, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfigurationSource.getSource(), DomainConfigurationUnitType.LIST_VIEW, FragmentBuilder.class ) ) {
			return;
		}

		final Fragment listViewFragment = domainConfigurationSource.getListViewFragment();
		Set<FieldMetadata> fields = listViewFragment.getFields();

		for ( FieldMetadata field : fields ) {
			if ( domainTypePropertyValidator.isInvalidProperty( field.getFieldName(), domainConfigurationSource.getDomainTypeEntityMetadata() ) ) {
				problemReporter.error( new InvalidPropertyConfigurationProblem( field.getFieldName(), domainConfigurationSource, DomainConfigurationUnitType.LIST_VIEW ) );
			}
		}
	}

	void validateFilters( final DomainConfigurationSource<Class> domainConfigurationSource, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfigurationSource.getSource(), DomainConfigurationUnitType.FILTERS, FilterBuilder.class ) ) {
			return;
		}

		for ( Filter filter : domainConfigurationSource.getFilters() ) {
			final String fieldName = filter.getFieldName();
			if ( domainTypePropertyValidator.isInvalidProperty( fieldName, domainConfigurationSource.getDomainTypeEntityMetadata() ) ) {
				problemReporter.warning( new InvalidPropertyConfigurationProblem( fieldName, domainConfigurationSource, DomainConfigurationUnitType.FILTERS ) );
			}
		}
	}
}