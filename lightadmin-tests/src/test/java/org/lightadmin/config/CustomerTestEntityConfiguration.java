package org.lightadmin.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.FieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.DomainTypePredicates;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.test.model.TestCustomer;
import org.lightadmin.test.scope.DeleteTestSpecification;
import org.lightadmin.test.scope.DummySpecification;

import static org.lightadmin.core.config.domain.scope.ScopeMetadataUtils.*;

@SuppressWarnings("unused")
@Administration(TestCustomer.class)
public class CustomerTestEntityConfiguration {

	public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
		return configurationBuilder.nameField( "firstname" ).build();
	}

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder.screenName( "Administration of Test Customers Administration" ).build();
	}

	public static FieldSetConfigurationUnit listView( final FieldSetConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder.field( "firstname" ).caption( "First Name" ).field( "lastname" ).caption( "Last Name" ).field( "emailAddress" ).caption( "Email Address" ).build();
	}

	public static ScopesConfigurationUnit scopes( final ScopesConfigurationUnitBuilder scopeBuilder ) {
		return scopeBuilder.scope( "All", all() ).defaultScope().scope( "Buyers", filter( DomainTypePredicates.alwaysTrue() ) ).scope( "Sellers", specification( new DummySpecification() ) ).scope( "Deletion Test", specification( new DeleteTestSpecification() ) ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder.filter( "First Name", "firstname" ).filter( "Last Name", "lastname" ).filter( "Email Address", "emailAddress" ).build();
	}

}
