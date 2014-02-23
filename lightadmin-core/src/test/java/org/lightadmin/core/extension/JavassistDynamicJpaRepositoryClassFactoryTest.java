package org.lightadmin.core.extension;

import org.junit.Before;
import org.junit.Test;

public class JavassistDynamicJpaRepositoryClassFactoryTest {

    private JavassistDynamicJpaRepositoryClassFactory testee;

    @Before
    public void setUp() {
        testee = new JavassistDynamicJpaRepositoryClassFactory(new DynamicRepositoryBeanNameGenerator());
    }

    @Test
    public void testCreateDynamicRepositoryClass() {
        testee.createDynamicRepositoryClass(String.class, Long.class);
    }
}