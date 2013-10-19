package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.core.io.ResourceLoader;

public class DomainConfigurationSourceValidatorFactory {

    private final DomainTypeEntityMetadataResolver entityMetadataResolver;
    private final ResourceLoader resourceLoader;
    private final WebContext webContext;

    public DomainConfigurationSourceValidatorFactory(final DomainTypeEntityMetadataResolver entityMetadataResolver, ResourceLoader resourceLoader, WebContext webContext) {
        this.entityMetadataResolver = entityMetadataResolver;
        this.resourceLoader = resourceLoader;
        this.webContext = webContext;
    }

    public DomainConfigurationSourceValidator getValidator() {
        return new DomainConfigurationUnitsSourceValidator(entityMetadataResolver, resourceLoader, webContext);
    }
}