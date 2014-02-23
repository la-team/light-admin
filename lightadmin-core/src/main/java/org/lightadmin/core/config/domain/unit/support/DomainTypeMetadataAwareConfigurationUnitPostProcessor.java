package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataAware;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.util.ClassUtils;

public class DomainTypeMetadataAwareConfigurationUnitPostProcessor extends EntityMetadataResolverAwareConfigurationUnitPostProcessor {

    public DomainTypeMetadataAwareConfigurationUnitPostProcessor(final DomainTypeEntityMetadataResolver entityMetadataResolver) {
        super(entityMetadataResolver);
    }

    @Override
    public ConfigurationUnit postProcess(final ConfigurationUnit configurationUnit, ConfigurationUnits configurationUnits) {
        if (ClassUtils.isAssignableValue(DomainTypeEntityMetadataAware.class, configurationUnit)) {
            final DomainTypeEntityMetadata domainTypeEntityMetadata = resolveEntityMetadata(configurationUnit);

//            ((DomainTypeEntityMetadataAware) configurationUnit).setDomainTypeEntityMetadata(domainTypeEntityMetadata);
        }
        return configurationUnit;
    }
}