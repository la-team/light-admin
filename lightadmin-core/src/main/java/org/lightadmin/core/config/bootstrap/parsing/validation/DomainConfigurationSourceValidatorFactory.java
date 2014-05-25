package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mapping.context.MappingContext;

public class DomainConfigurationSourceValidatorFactory {

    private final MappingContext mappingContext;
    private final ResourceLoader resourceLoader;
    private final LightAdminConfiguration lightAdminConfiguration;

    public DomainConfigurationSourceValidatorFactory(LightAdminConfiguration lightAdminConfiguration, final MappingContext entityMetadataResolver, ResourceLoader resourceLoader) {
        mappingContext = entityMetadataResolver;
        this.resourceLoader = resourceLoader;
        this.lightAdminConfiguration = lightAdminConfiguration;
    }

    public DomainConfigurationSourceValidator getValidator() {
        return new DomainConfigurationUnitsSourceValidator(mappingContext, resourceLoader, lightAdminConfiguration);
    }
}