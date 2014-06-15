package org.lightadmin.api.config.unit;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

public interface EntityMetadataConfigurationUnit extends ConfigurationUnit {

    Class<? extends AbstractRepositoryEventListener> getRepositoryEventListener();

    EntityNameExtractor getNameExtractor();

    String getSingularName();

    String getPluralName();
}