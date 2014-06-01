package org.springframework.data.rest.webmvc.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

public class DynamicPersistentEntityJackson2Module extends SimpleModule {

    public DynamicPersistentEntityJackson2Module(GlobalAdministrationConfiguration globalAdministrationConfiguration, LightAdminConfiguration lightAdminConfiguration, RepositoryRestConfiguration config) {
        super(new Version(1, 0, 0, null, "org.lightadmin", "jackson-module"));

        addSerializer(new DynamicPersistentEntityResourceSerializer(globalAdministrationConfiguration, lightAdminConfiguration, config));
    }
}