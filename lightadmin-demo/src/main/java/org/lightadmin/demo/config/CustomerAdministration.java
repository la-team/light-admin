package org.lightadmin.demo.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.common.FieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.common.PersistentFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.DomainTypePredicates;
import org.lightadmin.core.config.domain.scope.DomainTypeSpecification;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.demo.model.Customer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.lightadmin.core.config.domain.renderer.Renderers.select;
import static org.lightadmin.core.config.domain.scope.ScopeMetadataUtils.*;

@SuppressWarnings( "unused" )
@Administration( Customer.class )
public class CustomerAdministration {

	public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
		return configurationBuilder.nameField( "firstname" ).build();
	}

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
			.screenName( "Customers Administration" )
			.menuName( "Customers" ).build();
	}

	public static FieldSetConfigurationUnit listView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "firstname" ).caption( "First Name" )
			.field( "lastname" ).caption( "Last Name" )
			.field( "emailAddress" ).caption( "Email Address" )
			.field( "addresses" ).caption( "Addresses" )
			.field( "discountPrograms" ).caption( "Discount Programs" ).build();
	}

	public static FieldSetConfigurationUnit quickView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "firstname" ).caption( "First Name" )
			.field( "lastname" ).caption( "Last Name" )
			.field( "discountPrograms" ).caption( "Discount Programs" )
			.field( "addresses" ).caption( "Addresses" ).build();
	}

	public static FieldSetConfigurationUnit showView( final FieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "firstname" ).caption( "First Name" )
			.field( "lastname" ).caption( "Last Name" )
			.field( "emailAddress" ).caption( "Email Address" )
			.field( "discountPrograms" ).caption( "Discount Programs" )
			.field( "addresses" ).caption( "Addresses" ).build();
	}

	public static FieldSetConfigurationUnit formView( final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
				.field( "firstname" ).caption( "First Name" )
				.field( "lastname" ).caption( "Last Name" )
				.field( "emailAddress" ).caption( "Email Address" )
				.field( "discountPrograms" ).caption( "Discount Programs" )
				.field( "addresses" ).caption( "Addresses" ).build();
	}

	public static ScopesConfigurationUnit scopes( final ScopesConfigurationUnitBuilder scopeBuilder ) {
		return scopeBuilder
			.scope( "All", all() ).defaultScope()
			.scope( "Buyers", filter( DomainTypePredicates.alwaysTrue() ) )
			.scope( "Sellers", specification( customerNameEqDave() ) ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.filter( "ID", "id" )
			.filter( "First Name", "firstname" ).renderer( select( new String[] { "Yes", "No" } ))
			.filter( "Last Name", "lastname" )
			.filter( "Email Address", "emailAddress" )
			.filter( "Addresses", "addresses" )
			.filter( "Discount Programs", "discountPrograms" ).build();
	}

	public static DomainTypeSpecification<Customer> customerNameEqDave() {
		return new DomainTypeSpecification<Customer>() {
			@Override
			public Predicate toPredicate( final Root<Customer> root, final CriteriaQuery<?> query, final CriteriaBuilder cb ) {
				return cb.equal( root.get( "firstname" ), "Dave" );
			}
		};
	}
}