package org.lightadmin.config;

import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.*;
import org.lightadmin.api.config.utils.DomainTypePredicates;
import org.lightadmin.test.model.TestCustomer;
import org.lightadmin.test.scope.DeleteTestSpecification;
import org.lightadmin.test.scope.DummySpecification;

import static org.lightadmin.api.config.utils.ScopeMetadataUtils.*;

@SuppressWarnings("unused")
@Administration(TestCustomer.class)
public class CustomerTestEntityConfiguration {

    public static EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder.nameField("firstname").pluralName("Test Customers Domain").build();
    }

    public static ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("Administration of Test Customers Administration").build();
    }

    public static FieldSetConfigurationUnit listView(final FieldSetConfigurationUnitBuilder listViewBuilder) {
        return listViewBuilder.field("firstname").caption("First Name").field("lastname").caption("Last Name").field("emailAddress").caption("Email Address").build();
    }

    public static ScopesConfigurationUnit scopes(final ScopesConfigurationUnitBuilder scopeBuilder) {
        return scopeBuilder.scope("All", all()).defaultScope().scope("Buyers", filter(DomainTypePredicates.alwaysTrue())).scope("Sellers", specification(new DummySpecification())).scope("DeletionTest", specification(new DeleteTestSpecification())).build();
    }

    public static FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder.filter("First Name", "firstname").filter("Last Name", "lastname").filter("Email Address", "emailAddress").build();
    }

}
