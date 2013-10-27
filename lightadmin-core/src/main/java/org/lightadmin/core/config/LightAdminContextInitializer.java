package org.lightadmin.core.config;

import org.lightadmin.core.config.context.LightAdminContextConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class LightAdminContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
        applicationContext.register(LightAdminContextConfiguration.class);
    }

}
