package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.util.Assert;

class DomainConfigurationClassDTOSource implements DomainConfigurationSource<DomainConfigurationClassDTO> {

	private final DomainTypeEntityMetadata domainTypeEntityMetadata;
	private final DomainConfigurationClassDTO domainConfigurationClassDTO;

	public DomainConfigurationClassDTOSource( final DomainTypeEntityMetadata domainTypeEntityMetadata, DomainConfigurationClassDTO domainConfigurationClassDTO ) {
		Assert.notNull( domainTypeEntityMetadata );
		Assert.notNull( domainConfigurationClassDTO );

		this.domainTypeEntityMetadata = domainTypeEntityMetadata;
		this.domainConfigurationClassDTO = domainConfigurationClassDTO;
	}

	@Override
	public DomainConfigurationClassDTO getSource() {
		return domainConfigurationClassDTO;
	}

	@Override
	public Class<?> getDomainType() {
		return domainConfigurationClassDTO.getDomainType();
	}

	@Override
	public String getConfigurationName() {
		return String.format( "%sConfiguration", getDomainType().getSimpleName() );
	}

	@Override
	public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
		return domainTypeEntityMetadata;
	}

	@Override
	public Filters getFilters() {
		return domainConfigurationClassDTO.getFilters();
	}

	@Override
	public Scopes getScopes() {
		return domainConfigurationClassDTO.getScopes();
	}

	@Override
	public Fragment getListViewFragment() {
		return domainConfigurationClassDTO.getListViewFragment();
	}

	@Override
	public ScreenContext getScreenContext() {
		return domainConfigurationClassDTO.getScreenContext();
	}

	@Override
	public EntityConfiguration getEntityConfiguration() {
		return domainConfigurationClassDTO.getEntityConfiguration();
	}
}