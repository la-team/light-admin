package org.lightadmin.core.config.context;

import org.lightadmin.core.extension.DynamicRepositoryBeanNameGenerator;
import org.lightadmin.core.extension.DynamicRepositoryClassFactory;
import org.lightadmin.core.extension.JavassistDynamicJpaRepositoryClassFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LightAdminDynamicRepositoryConfiguration {

    @Bean
    public DynamicRepositoryClassFactory dynamicJpaRepositoryClassFactory() {
        return new JavassistDynamicJpaRepositoryClassFactory(dynamicRepositoryBeanNameGenerator());
    }

    @Bean
    public DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator() {
        return new DynamicRepositoryBeanNameGenerator();
    }
}