package org.lightadmin.core.config.domain.scope;

import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;

public class DefaultScopesConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<ScopesConfigurationUnit> implements ScopesConfigurationUnitBuilder {

	private final List<ScopeMetadata> scopesMetadata = newLinkedList();

	private ScopeMetadata currentScope = null;

	public DefaultScopesConfigurationUnitBuilder( final Class<?> domainType ) {
		super( domainType );
	}

	@Override
	public ScopesConfigurationUnitBuilder scope( final String name, final ScopeMetadata scope ) {
		currentScope = ScopeMetadataUtils.scope( scope );
		scopesMetadata.add( currentScope.name( name ) );
		return this;
	}

	@Override
	public ScopesConfigurationUnitBuilder scope( final ScopeMetadata scope ) {
		currentScope = ScopeMetadataUtils.scope( scope );
		scopesMetadata.add( currentScope );
		return this;
	}

	@Override
	public ScopesConfigurationUnitBuilder defaultScope() {
		if ( currentScope != null ) {
			currentScope.defaultScope( true );
			currentScope = null;
		}
		return this;
	}

	@Override
	public ScopesConfigurationUnit build() {
		if ( scopesMetadata.isEmpty() ) {
			return new DefaultScopesConfigurationUnit( getDomainType(), newArrayList( ScopeMetadataUtils.all().defaultScope( true ) ) );
		}
		return new DefaultScopesConfigurationUnit( getDomainType(), scopesMetadata );
	}
}