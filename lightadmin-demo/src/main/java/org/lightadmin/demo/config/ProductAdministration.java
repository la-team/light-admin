package org.lightadmin.demo.config;

import org.lightadmin.api.config.annotation.Administration;
import org.lightadmin.api.config.builder.*;
import org.lightadmin.api.config.unit.*;
import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.demo.model.Product;

import static org.lightadmin.api.config.utils.Editors.enumeration;
import static org.lightadmin.api.config.utils.Editors.wysiwyg;
import static org.lightadmin.api.config.utils.EnumElement.element;

@SuppressWarnings("unused")
@Administration(Product.class)
public class ProductAdministration {

    public static EntityMetadataConfigurationUnit configuration(EntityMetadataConfigurationUnitBuilder configurationBuilder) {
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

    public static ScreenContextConfigurationUnit screenContext(ScreenContextConfigurationUnitBuilder screenContextBuilder) {
        return screenContextBuilder.screenName("Products Administration").build();
    }

    public static FieldSetConfigurationUnit listView(FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("name").caption("Name")
                .field("type").caption("Type").enumeration(
                        element("SP", "S"),
                        element("TB", "T"),
                        element("OT", "O"))
                .field("description").caption("Description")
                .field("price").caption("Price")
                .field("retired").caption("Out of production")
                .field("releaseDate").caption("Released on")
                .field("picture").caption("Picture").build();
    }

    public static FieldSetConfigurationUnit showView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("name").caption("Name")
                .field("type").caption("Type")
                .field("description").caption("Description")
                .field("price").caption("Price")
                .field("retired").caption("Out of production")
                .field("releaseDate").caption("Released on")
                .field("picture").caption("Picture").build();
    }

    public static FieldSetConfigurationUnit formView(final PersistentFieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("name").caption("Name")
                .field("type").caption("Type")
                .field("description").caption("Description").editor(wysiwyg())
                .field("price").caption("Price")
                .field("releaseDate").caption("Released on")
                .field("retired").caption("Out of production")
                .field("picture").caption("Picture").build();
    }

    public static FieldSetConfigurationUnit quickView(final FieldSetConfigurationUnitBuilder fragmentBuilder) {
        return fragmentBuilder
                .field("uuid").caption("UUID")
                .field("name").caption("Name")
                .field("type").caption("Type")
                .field("description").caption("Description")
                .field("retired").caption("Out of production")
                .field("releaseDate").caption("Released on")
                .field("picture").caption("Picture").build();
    }

    public static FiltersConfigurationUnit filters(final FiltersConfigurationUnitBuilder filterBuilder) {
        return filterBuilder
                .filter("Name", "name")
                .filter("Description", "description")
                .filter("Price", "price")
                .filter("Released on", "releaseDate")
                .filter("Retired", "retired").build();
    }

    public static SidebarsConfigurationUnit sidebars(final SidebarsConfigurationUnitBuilder sidebarsBuilder) {
        return sidebarsBuilder
                .sidebar("Custom Sidebar", "/WEB-INF/admin/sidebars/sidebar.jsp")
                .build();
    }
}
