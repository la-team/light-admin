package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.support.DomainTypeConfigurationUnit;
import org.lightadmin.core.config.domain.support.DomainTypeEntityMetadataAware;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newLinkedHashSet;

public class DefaultFiltersConfigurationUnit extends DomainTypeConfigurationUnit implements FiltersConfigurationUnit, DomainTypeEntityMetadataAware {

	private final Set<FilterMetadata> filtersMetadata;

	DefaultFiltersConfigurationUnit( Class<?> domainType, final Set<FilterMetadata> filtersMetadata ) {
		super( domainType );

		Assert.notNull( filtersMetadata );

		this.filtersMetadata = newLinkedHashSet( filtersMetadata );
	}

	@Override
	public Iterator<FilterMetadata> iterator() {
		return filtersMetadata.iterator();
	}

	public int size() {
		return filtersMetadata.size();
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.FILTERS;
	}

	@Override
	public void setDomainTypeEntityMetadata( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		for ( FilterMetadata filterMetadata : newHashSet(filtersMetadata) ) {
			filtersMetadata.add( FilterMetadataUtils.decorateAttributeMetadata( filterMetadata, domainTypeEntityMetadata.getAttribute( filterMetadata.getFieldName() ) ) );
		}
	}
}