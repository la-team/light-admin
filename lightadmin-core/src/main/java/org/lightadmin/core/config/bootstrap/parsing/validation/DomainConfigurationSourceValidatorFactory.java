package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.core.io.ResourceLoader;

public class DomainConfigurationSourceValidatorFactory {

    private final DomainTypeEntityMetadataResolver entityMetadataResolver;
    private final ResourceLoader resourceLoader;

    public DomainConfigurationSourceValidatorFactory(final DomainTypeEntityMetadataResolver entityMetadataResolver, ResourceLoader resourceLoader) {
        this.entityMetadataResolver = entityMetadataResolver;
        this.resourceLoader = resourceLoader;
    }

    public DomainConfigurationSourceValidator getValidator() {
        return new DomainConfigurationUnitsSourceValidator(entityMetadataResolver, resourceLoader);
    }
}