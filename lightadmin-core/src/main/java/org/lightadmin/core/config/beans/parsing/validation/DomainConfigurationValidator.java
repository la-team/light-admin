package org.lightadmin.core.config.beans.parsing.validation;

import org.lightadmin.core.config.beans.parsing.ConfigurationUnitPropertyFilter;
import org.lightadmin.core.config.beans.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.beans.parsing.InvalidPropertyConfigurationProblem;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.filter.Filter;
import org.lightadmin.core.config.domain.filter.FilterBuilder;
import org.lightadmin.core.config.domain.fragment.FieldMetadata;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;

import java.util.Set;

import static org.lightadmin.core.util.DomainConfigurationUtils.isNotConfigurationUnitDefined;

public class DomainConfigurationValidator {

	private DomainTypePropertyValidatorInterface domainTypePropertyValidator;

	DomainConfigurationValidator( final DomainTypePropertyValidatorInterface domainTypePropertyValidator ) {
		this.domainTypePropertyValidator = domainTypePropertyValidator;
	}

	public DomainConfigurationValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.domainTypePropertyValidator = new DomainTypePropertyValidator( entityMetadataResolver );
	}

	public void validate( DomainConfigurationInterface domainConfiguration, ProblemReporter problemReporter ) {
		validateScreenContext( domainConfiguration, problemReporter );

		validateEntityConfiguration( domainConfiguration, problemReporter );

		validateFilters( domainConfiguration, problemReporter );

		validateListView( domainConfiguration, problemReporter );
	}

	public ConfigurationUnitPropertyFilter validPropertyFilter( DomainConfigurationInterface domainConfiguration ) {
		return new ValidPropertyFilter( domainConfiguration.getDomainTypeEntityMetadata() );
	}

	void validateEntityConfiguration( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfiguration.getConfigurationClass(), DomainConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class ) ) {
			problemReporter.warning( new DomainConfigurationProblem( domainConfiguration, DomainConfigurationUnit.CONFIGURATION, "It's better to define \"configuration\" unit or system will use DomainTypeClass#getSimpleName for entity string representation!" ) );
		}
	}

	void validateScreenContext( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfiguration.getConfigurationClass(), DomainConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class ) ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfiguration, DomainConfigurationUnit.SCREEN_CONTEXT, "You need to define \"screenContext\" configuration unit!" ) );
		}
	}

	void validateListView( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfiguration.getConfigurationClass(), DomainConfigurationUnit.LIST_VIEW, FragmentBuilder.class ) ) {
			return;
		}

		final Fragment listViewFragment = domainConfiguration.getListViewFragment();
		Set<FieldMetadata> fields = listViewFragment.getFields();

		for ( FieldMetadata field : fields ) {
			if ( domainTypePropertyValidator.isInvalidProperty( field.getFieldName(), domainConfiguration.getDomainTypeEntityMetadata() ) ) {
				problemReporter.warning( new InvalidPropertyConfigurationProblem( field.getFieldName(), domainConfiguration, DomainConfigurationUnit.LIST_VIEW ) );
			}
		}
	}

	void validateFilters( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( domainConfiguration.getConfigurationClass(), DomainConfigurationUnit.FILTERS, FilterBuilder.class ) ) {
			return;
		}

		for ( Filter filter : domainConfiguration.getFilters() ) {
			final String fieldName = filter.getFieldName();
			if ( domainTypePropertyValidator.isInvalidProperty( fieldName, domainConfiguration.getDomainTypeEntityMetadata() ) ) {
				problemReporter.warning( new InvalidPropertyConfigurationProblem( fieldName, domainConfiguration, DomainConfigurationUnit.FILTERS ) );
			}
		}
	}

	private class ValidPropertyFilter implements ConfigurationUnitPropertyFilter {

		private final DomainTypeEntityMetadata entityMetadata;

		private ValidPropertyFilter( final DomainTypeEntityMetadata entityMetadata ) {
			this.entityMetadata = entityMetadata;
		}

		@Override
		public boolean apply( final String property ) {
			return domainTypePropertyValidator.isValidProperty( property, entityMetadata );
		}
	}
}