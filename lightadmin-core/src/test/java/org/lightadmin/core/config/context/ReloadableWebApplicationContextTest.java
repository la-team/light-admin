package org.lightadmin.core.config.context;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class ReloadableWebApplicationContextTest {

    private ReloadableWebApplicationContext applicationContext;

    @Before
    public void setUp() {
        applicationContext = new ReloadableWebApplicationContext();
        applicationContext.addBeanFactoryPostProcessor(new ContextPostProcessor());
        applicationContext.register(ContextConfiguration.class);
        applicationContext.refresh();
    }

    @Test
    public void testRefresh() {

        Integer refreshCounter1 = applicationContext.getBean("refreshCounter", Integer.class);
        Integer reloadCounter1 = applicationContext.getBean("reloadCounter", Integer.class);

        applicationContext.refresh();

        Integer reloadCounter2 = applicationContext.getBean("reloadCounter", Integer.class);
        assertEquals(reloadCounter1 + 1, reloadCounter2.intValue());

        Integer refreshCounter2 = applicationContext.getBean("refreshCounter", Integer.class);
        assertEquals(refreshCounter1 + 1, refreshCounter2.intValue());
    }


    @Test
    public void testReloadSingletons() {

        Integer refreshCounter1 = applicationContext.getBean("refreshCounter", Integer.class);
        Integer reloadCounter1 = applicationContext.getBean("reloadCounter", Integer.class);

        applicationContext.reloadSingletons();

        Integer reloadCounter2 = applicationContext.getBean("reloadCounter", Integer.class);
        assertEquals(reloadCounter1 + 1, reloadCounter2.intValue());

        Integer refreshCounter2 = applicationContext.getBean("refreshCounter", Integer.class);
        assertEquals(refreshCounter1, refreshCounter2);
    }

    @Configuration
    public static class ContextConfiguration {
        private static final AtomicInteger counter = new AtomicInteger();
        @Bean
        public Integer reloadCounter() {
            return counter.incrementAndGet();
        }
    }

    private static class ContextPostProcessor implements BeanDefinitionRegistryPostProcessor {
        private static final AtomicInteger counter = new AtomicInteger();
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        }
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Integer.class).
                    addConstructorArgValue(counter.incrementAndGet()).
                    getBeanDefinition();
            registry.registerBeanDefinition("refreshCounter", beanDefinition);
        }
    }

}
