package org.lightadmin.core.config.context;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.extension.DynamicRepositoryBeanNameGenerator;
import org.lightadmin.core.extension.DynamicRepositoryClassFactory;
import org.lightadmin.core.extension.JavassistDynamicJpaRepositoryClassFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@Configuration
public class LightAdminDynamicRepositoryConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GlobalAdministrationConfiguration globalAdministrationConfiguration;

    //
    @Bean
    public String testBean() {
        Map<Class<?>, DomainTypeBasicConfiguration> domainTypeConfigurations = globalAdministrationConfiguration.getDomainTypeConfigurations();

        return "Str";
    }

//    @Bean
//    public DynamicRepositoryBeanFactory dynamicRepositoryBeanFactory() {
//        return new DefaultDynamicRepositoryBeanFactory(dynamicRepositoryBeanNameGenerator(), repositoryFactoryBeanCreator());
//    }

//    @Bean
//    public RepositoryFactoryBeanCreator repositoryFactoryBeanCreator() {
//        return new JpaRepositoryFactoryBeanCreator(dynamicJpaRepositoryClassFactory(), this.entityManager, this.webApplicationContext);
//    }

    @Bean
    public DynamicRepositoryClassFactory dynamicJpaRepositoryClassFactory() {
        return new JavassistDynamicJpaRepositoryClassFactory(dynamicRepositoryBeanNameGenerator());
    }

    @Bean
    public DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator() {
        return new DynamicRepositoryBeanNameGenerator();
    }


}