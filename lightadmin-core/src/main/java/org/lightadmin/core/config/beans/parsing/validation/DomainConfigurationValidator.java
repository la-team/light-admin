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
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.config.domain.fragment.TableFragment;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.util.Pair;

import java.util.Set;

import static org.lightadmin.core.util.DomainConfigurationUtils.isConfigurationUnitDefined;
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
		final Class configurationClass = domainConfiguration.getConfigurationClass();

		if ( isNotConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class ) ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfiguration, "You need to define \"screenContext\" configuration unit!" ) );
		}

		if ( isNotConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class ) ) {
			problemReporter.warning( new DomainConfigurationProblem( domainConfiguration, "It's better to define \"configuration\" unit or system will use DomainTypeClass#getSimpleName for entity string representation!" ) );
		}

		if ( isConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.FILTERS, FilterBuilder.class ) ) {
			validateFilters( domainConfiguration, problemReporter );
		}

		if ( isConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.LIST_VIEW, FragmentBuilder.class ) ) {
			validateListView( domainConfiguration, problemReporter );
		}
	}

	public ConfigurationUnitPropertyFilter validPropertyFilter( DomainConfigurationInterface domainConfiguration ) {
		return new ValidPropertyFilter( domainConfiguration.getDomainTypeEntityMetadata() );
	}

	private void validateListView( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
		final TableFragment listViewFragment = ( TableFragment ) domainConfiguration.getListViewFragment();
		final Set<Pair<String, String>> columnsPairs = listViewFragment.getColumns();

		for ( Pair<String, String> columnsPair : columnsPairs ) {
			if ( domainTypePropertyValidator.isInvalidProperty( columnsPair.first, domainConfiguration.getDomainTypeEntityMetadata() ) ) {
				problemReporter.warning( new InvalidPropertyConfigurationProblem( columnsPair.first, domainConfiguration, DomainConfigurationUnit.LIST_VIEW ) );
			}
		}
	}

	private void validateFilters( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
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