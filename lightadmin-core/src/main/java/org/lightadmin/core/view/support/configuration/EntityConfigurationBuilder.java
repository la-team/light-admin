package org.lightadmin.core.view.support.configuration;

import com.google.common.base.Function;
import org.lightadmin.core.view.support.Builder;

public interface EntityConfigurationBuilder  extends Builder<EntityConfiguration>  {

	EntityConfigurationBuilder nameField( String nameField );

	EntityConfigurationBuilder nameExtractor( Function<?, String> nameExtractor );

}