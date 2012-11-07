package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;

public class FragmentListViewConfigurationUnit extends DomainTypeConfigurationUnit implements ListViewConfigurationUnit {

	private final Fragment fragment;

	FragmentListViewConfigurationUnit( Class<?> domainType, final Fragment fragment ) {
		super( domainType );

		this.fragment = fragment;
	}

	@Override
	public Fragment getFragment() {
		return fragment;
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.LIST_VIEW;
	}
}