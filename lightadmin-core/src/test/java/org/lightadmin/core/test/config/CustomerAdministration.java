package org.lightadmin.core.test.config;

import com.google.common.base.Predicates;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnitBuilder;
import org.lightadmin.core.test.model.Customer;
import org.springframework.data.jpa.domain.Specification;

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

	public static ListViewConfigurationUnit listView( final ListViewConfigurationUnitBuilder fragmentBuilder ) {
		return fragmentBuilder
			.field( "firstname" ).alias( "First Name")
			.field( "lastname" ).alias("Last Name")
			.field( "emailAddress" ).alias( "Email Address" ).build();
	}

	public static ScopesConfigurationUnit scopes( final ScopesConfigurationUnitBuilder scopeBuilder ) {
		return scopeBuilder
			.scope( "All", all() ).defaultScope()
			.scope( "Buyers", filter( Predicates.alwaysTrue() ) )
			.scope( "Sellers", specification( customerNameEqDave() ) ).build();
	}

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
			.field( "firstname" ).renderer( select( new String[] { "Yes", "No" } ))
			.field( "lastname" ).build();
	}

	public static Specification<Customer> customerNameEqDave() {
		return new Specification<Customer>() {
			@Override
			public Predicate toPredicate( final Root<Customer> root, final CriteriaQuery<?> query, final CriteriaBuilder cb ) {
				return cb.equal( root.get( "firstname" ), "Dave" );
			}
		};
	}
}