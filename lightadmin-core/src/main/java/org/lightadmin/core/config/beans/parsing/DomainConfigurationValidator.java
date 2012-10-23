package org.lightadmin.core.config.beans.parsing;

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
import org.lightadmin.core.util.DomainTypeBeanUtils;
import org.lightadmin.core.util.Pair;

import java.util.Set;

import static org.lightadmin.core.util.DomainConfigurationUtils.isConfigurationUnitDefined;
import static org.lightadmin.core.util.DomainConfigurationUtils.isNotConfigurationUnitDefined;

class DomainConfigurationValidator {

	private DomainTypeEntityMetadataResolver entityMetadataResolver;

	public DomainConfigurationValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}

	public void validate( DomainConfigurationInterface domainConfiguration, ProblemReporter problemReporter ) {
		domainConfiguration.getDomainTypeEntityMetadata();
		final Class configurationClass = domainConfiguration.getConfigurationClass();

		if ( isNotConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class ) ) {
			problemReporter.error( new DomainConfigurationProblem( domainConfiguration, "You need to define screenContext configuration unit!" ) );
		}

		if ( isNotConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class ) ) {
			problemReporter.warning( new DomainConfigurationProblem( domainConfiguration, "It's better to define Entity configuration unit or system will use DomainTypeClass#getSimpleName for entity string representation!" ) );
		}

		if ( isConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.FILTERS, FilterBuilder.class ) ) {
			validateFilters( domainConfiguration, problemReporter );
		}

		if ( isConfigurationUnitDefined( configurationClass, DomainConfigurationUnit.LIST_VIEW, FragmentBuilder.class ) ) {
			validateListView( domainConfiguration, problemReporter );
		}
	}

	private void validateListView( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
		final TableFragment listViewFragment = ( TableFragment ) domainConfiguration.getListViewFragment();
		final Set<Pair<String, String>> columnsPairs = listViewFragment.getColumns();

		for ( Pair<String, String> columnsPair : columnsPairs ) {
			if ( DomainTypeBeanUtils.isInvalidProperty( columnsPair.first, domainConfiguration.getDomainTypeEntityMetadata(), entityMetadataResolver ) ) {
				problemReporter.warning( new InvalidPropertyConfigurationProblem( columnsPair.first, domainConfiguration, DomainConfigurationUnit.LIST_VIEW ) );
			}
		}
	}

	private void validateFilters( final DomainConfigurationInterface domainConfiguration, final ProblemReporter problemReporter ) {
		for ( Filter filter : domainConfiguration.getFilters() ) {
			final String fieldName = filter.getFieldName();
			if ( DomainTypeBeanUtils.isInvalidProperty( fieldName, domainConfiguration.getDomainTypeEntityMetadata(), entityMetadataResolver ) ) {
				problemReporter.warning( new InvalidPropertyConfigurationProblem( fieldName, domainConfiguration, DomainConfigurationUnit.FILTERS ) );
			}
		}
	}

	public ConfigurationUnitPropertyFilter validPropertyFilter( DomainConfigurationInterface domainConfiguration ) {
		return new ValidPropertyFilter( domainConfiguration.getDomainTypeEntityMetadata(), entityMetadataResolver );
	}

	static class ValidPropertyFilter implements ConfigurationUnitPropertyFilter {

		private final DomainTypeEntityMetadata entityMetadata;
		private final DomainTypeEntityMetadataResolver entityMetadataResolver;

		private ValidPropertyFilter( final DomainTypeEntityMetadata entityMetadata, final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
			this.entityMetadata = entityMetadata;
			this.entityMetadataResolver = entityMetadataResolver;
		}

		@Override
		public boolean apply( final String property ) {
			return !DomainTypeBeanUtils.isInvalidProperty( property, entityMetadata, entityMetadataResolver );
		}
	}
}