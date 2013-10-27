package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.core.io.ResourceLoader;

public class DomainConfigurationSourceValidatorFactory {

    private final DomainTypeEntityMetadataResolver entityMetadataResolver;
    private final ResourceLoader resourceLoader;
    private final LightAdminConfiguration lightAdminConfiguration;

    public DomainConfigurationSourceValidatorFactory(LightAdminConfiguration lightAdminConfiguration, final DomainTypeEntityMetadataResolver entityMetadataResolver, ResourceLoader resourceLoader) {
        this.entityMetadataResolver = entityMetadataResolver;
        this.resourceLoader = resourceLoader;
        this.lightAdminConfiguration = lightAdminConfiguration;
    }

    public DomainConfigurationSourceValidator getValidator() {
        return new DomainConfigurationUnitsSourceValidator(entityMetadataResolver, resourceLoader, lightAdminConfiguration);
    }
}