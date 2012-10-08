package org.lightadmin.core.view.support.configuration;

import org.lightadmin.core.util.Transformer;
import org.lightadmin.core.view.support.Builder;

public interface EntityConfigurationBuilder extends Builder<EntityConfiguration> {

	EntityConfigurationBuilder nameField( String nameField );

	EntityConfigurationBuilder nameExtractor( Transformer<?, String> nameExtractor );

}