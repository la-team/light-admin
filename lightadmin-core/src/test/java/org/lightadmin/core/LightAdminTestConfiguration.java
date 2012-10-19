package org.lightadmin.core;

import org.lightadmin.core.config.LightAdminDataConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import( {PersistanceConfiguration.class, LightAdminDataConfiguration.class} )
public class LightAdminTestConfiguration {

}