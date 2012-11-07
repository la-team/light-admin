package org.lightadmin.core.config.domain.context;

import org.lightadmin.core.config.domain.support.ConfigurationUnit;

public interface ScreenContextConfigurationUnit extends ConfigurationUnit {

	String getScreenName();

	String getMenuItemName();
}