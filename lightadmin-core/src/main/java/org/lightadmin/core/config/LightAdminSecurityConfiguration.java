package org.lightadmin.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource( "classpath*:META-INF/spring/spring-security.xml" )
public class LightAdminSecurityConfiguration {
}