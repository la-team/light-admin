package org.lightadmin.demo.config;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.*;
import org.lightadmin.demo.model.Product;

import static org.lightadmin.api.config.utils.Editors.wysiwyg;
import static org.lightadmin.api.config.utils.EnumElement.element;

@SuppressWarnings("unused")
public class ProductAdministration extends AdministrationConfiguration<Product> {

    public EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
        return configurationBuilder
                .nameField("name")
                .singularName("Product")
                .pluralName("Products")
                .field("type").enumeration(
                        element("SP", "Smartphone"),
                        element("TB", "Tablet"),
                        element("OT", "Other"))
                .build();
    }

    public ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("Products Administration").build();
    }

    public FieldSetConfigurationUnit listView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("name").caption("Name")
                .field("type").caption("Type").enumeration(
                        element("SP", "S"),
                        element("TB", "T"),
                        element("OT", "O"))
                .field("productType").caption("Product Type")
                .field("description").caption("Description")
                .field("price").caption("Price")
                .field("retired").caption("Out of production")
                .field("releaseDate").caption("Released on")
                .field("releaseTime").caption("Released on time")
                .field("picture").caption("Picture").build();
    }

    public FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("name").caption("Name")
                .field("type").caption("Type")
                .field("productType").caption("Product Type")
                .field("description").caption("Description")
                .field("price").caption("Price")
                .field("retired").caption("Out of production")
                .field("releaseDate").caption("Released on")
                .field("releaseTime").caption("Released on time")
                .field("picture").caption("Picture").build();
    }

    public FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("name").caption("Name")
                .field("type").caption("Type")
                .field("productType").caption("Product Type").enumeration(
                        element("SMARTPHONE", "Smartphone"),
                        element("TABLET", "Tablet"),
                        element("OTHER", "Other"))
                .field("description").caption("Description").editor(wysiwyg())
                .field("price").caption("Price")
                .field("releaseDate").caption("Released on")
                .field("releaseTime").caption("Released on time")
                .field("retired").caption("Out of production")
                .field("picture").caption("Picture").build();
    }

    public FieldSetConfigurationUnit quickView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("uuid").caption("UUID")
                .field("name").caption("Name")
                .field("type").caption("Type")
                .field("description").caption("Description")
                .field("retired").caption("Out of production")
                .field("releaseDate").caption("Released on")
                .field("picture").caption("Picture").build();
    }

    public FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Name", "name")
                .filter("Description", "description")
                .filter("Price", "price")
                .filter("Released on", "releaseDate")
                .filter("Released on time", "releaseTime")
                .filter("Retired", "retired").build();
    }

    public SidebarsConfigurationUnit sidebars(final SidebarsConfigurationUnitBuilder sidebarsBuilder) {
        return sidebarsBuilder
                .sidebar("Custom Sidebar", "/WEB-INF/lightadmin/sidebars/sidebar.jsp")
                .build();
    }
}
