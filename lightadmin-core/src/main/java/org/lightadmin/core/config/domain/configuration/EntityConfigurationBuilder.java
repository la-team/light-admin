package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.Builder;
import org.lightadmin.core.util.Transformer;

public interface EntityConfigurationBuilder extends Builder<EntityConfiguration> {

	EntityConfigurationBuilder nameField( String nameField );

	EntityConfigurationBuilder nameExtractor( Transformer<?, String> nameExtractor );

}