package org.lightadmin.core.extension;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public class JavassistDynamicJpaRepositoryClassFactoryTest {

    private JavassistDynamicJpaRepositoryClassFactory testee;

    @Before
    public void setUp() {
        testee = new JavassistDynamicJpaRepositoryClassFactory(new DynamicRepositoryBeanNameGenerator());
    }

    @Test
    public void testCreateDynamicRepositoryClass() {
        Class<JpaRepository<String, Long>> dynamicRepositoryClass = testee.createDynamicRepositoryClass(String.class, Long.class);
        Class<JpaRepository<String, Long>> dynamicRepositoryClass1 = testee.createDynamicRepositoryClass(String.class, Long.class);
        Class<JpaRepository<String, Long>> dynamicRepositoryClass2 = testee.createDynamicRepositoryClass(String.class, Long.class);
    }
}