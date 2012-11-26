package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.TableListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.show.DefaultShowViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.util.ClassUtils;

import java.util.Collection;

public class EmptyConfigurationUnitPostProcessor extends EntityMetadataResolverAwareConfigurationUnitPostProcessor {

	public EmptyConfigurationUnitPostProcessor( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		super( entityMetadataResolver );
	}

	@Override
	public ConfigurationUnit postProcess( final ConfigurationUnit configurationUnit ) {
		if ( isEmptyListViewConfigurationUnit( configurationUnit ) ) {
			return listViewWithPersistentFields( configurationUnit.getDomainType() );
		}

		if ( isEmptyShowViewConfigurationUnit( configurationUnit ) ) {
			return showViewWithPersistentFields( configurationUnit.getDomainType() );
		}

		return configurationUnit;
	}

	private ConfigurationUnit showViewWithPersistentFields( final Class<?> domainType ) {
		final DefaultShowViewConfigurationUnitBuilder showViewConfigurationUnitBuilder = new DefaultShowViewConfigurationUnitBuilder( domainType );

		final Collection<DomainTypeAttributeMetadata> attributes = resolveEntityMetadata( domainType ).getAttributes();
		for ( DomainTypeAttributeMetadata attribute : attributes ) {
			showViewConfigurationUnitBuilder.field( attribute.getName() );
		}

		return showViewConfigurationUnitBuilder.build();
	}

	private ListViewConfigurationUnit listViewWithPersistentFields( Class<?> domainType ) {
		final TableListViewConfigurationUnitBuilder listViewConfigurationUnitBuilder = new TableListViewConfigurationUnitBuilder( domainType );

		final Collection<DomainTypeAttributeMetadata> attributes = resolveEntityMetadata( domainType ).getAttributes();
		for ( DomainTypeAttributeMetadata attribute : attributes ) {
			listViewConfigurationUnitBuilder.field( attribute.getName() );
		}

		return listViewConfigurationUnitBuilder.build();
	}

	private boolean isEmptyListViewConfigurationUnit( ConfigurationUnit configurationUnit ) {
		return ClassUtils.isAssignableValue( ListViewConfigurationUnit.class, configurationUnit ) && ( ( ListViewConfigurationUnit ) configurationUnit ).getFragment().getFields().isEmpty();
	}

	private boolean isEmptyShowViewConfigurationUnit( ConfigurationUnit configurationUnit ) {
		return ClassUtils.isAssignableValue( ShowViewConfigurationUnit.class, configurationUnit ) && ( ( ShowViewConfigurationUnit ) configurationUnit ).iterator().hasNext();
	}
}