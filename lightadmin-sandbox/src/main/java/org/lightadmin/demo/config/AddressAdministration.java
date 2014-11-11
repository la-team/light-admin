package org.lightadmin.demo.config;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.demo.model.Address;

@SuppressWarnings("unused")
public class AddressAdministration extends AdministrationConfiguration<Address> {

    public EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder
                .nameExtractor(addressNameExtractor())
                .singularName("Address")
                .pluralName("Addresses").build();
    }

    public ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("Addresses Administration").build();
    }

    public FieldSetConfigurationUnit listView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("country").caption("Country")
                .field("city").caption("City")
                .field("street").caption("Street").build();
    }

    public FieldSetConfigurationUnit quickView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("country").caption("Country")
                .field("city").caption("City").build();
    }

    public FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("country").caption("Country")
                .field("city").caption("City")
                .field("street").caption("Street").build();
    }

    public FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("country").caption("Country")
                .field("city").caption("City")
                .field("street").caption("Street")
                .field("customer").caption("Customer").build();
    }

    public FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Country", "country")
                .filter("City", "city")
                .filter("Street", "street")
                .filter("Customer", "customer").build();
    }

    private static EntityNameExtractor<Address> addressNameExtractor() {
        return new EntityNameExtractor<Address>() {
            @Override
            public String apply(final Address address) {
                return String.format("%s, %s, %s", address.getCountry(), address.getCity(), address.getStreet());
            }
        };
    }
}