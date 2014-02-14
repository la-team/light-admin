package org.lightadmin.core.config.bootstrap.scanning;

import org.lightadmin.api.config.AdministrationConfiguration;
import org.lightadmin.api.config.annotation.Administration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class AdministrationClassScanner extends TypeFilterClassScanner {

    public AdministrationClassScanner() {
        super(new AnnotationTypeFilter(Administration.class), new AssignableTypeFilter(AdministrationConfiguration.class));
    }
}