package org.lightadmin.core.extension;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.context.ApplicationEvent;

public class AdministrationConfigurationRefreshedEvent extends ApplicationEvent {

    public AdministrationConfigurationRefreshedEvent(GlobalAdministrationConfiguration source) {
        super(source);
    }

    public GlobalAdministrationConfiguration getGlobalAdministrationConfiguration() {
        return (GlobalAdministrationConfiguration) super.getSource();
    }
}