package org.springframework.data.rest.webmvc.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;

import static com.fasterxml.jackson.core.Version.unknownVersion;

public class LightAdminJacksonModule extends SimpleModule {

    public LightAdminJacksonModule(GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        super(unknownVersion());

        setSerializerModifier(new DynamicFilePropertyOmittingSerializerModifier(globalAdministrationConfiguration));
    }
}