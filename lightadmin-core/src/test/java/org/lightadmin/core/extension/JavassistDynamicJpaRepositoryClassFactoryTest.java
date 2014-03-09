package org.lightadmin.core.extension;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class JavassistDynamicJpaRepositoryClassFactoryTest {

    private JavassistDynamicJpaRepositoryClassFactory testee;

    @Before
    public void setUp() {
        testee = new JavassistDynamicJpaRepositoryClassFactory(new DynamicRepositoryBeanNameGenerator());
    }

    @Test
    public void testCreateDynamicRepositoryClass() {
        Class<JpaRepository<String, Long>> dynamicRepositoryClass = testee.createDynamicRepositoryClass(String.class, Long.class);

        ParameterizedType baseType = (ParameterizedType) dynamicRepositoryClass.getGenericInterfaces()[0];
        Type[] baseTypeArgs = baseType.getActualTypeArguments();

        assertEquals(JpaRepository.class, baseType.getRawType());
        assertEquals(String.class, baseTypeArgs[0]);
        assertEquals(Long.class, baseTypeArgs[1]);
    }

    @Test
    public void testCreateDynamicRepositoryClass_multipleCalls() {
        Class<JpaRepository<String, Long>> dynamicRepositoryClass1 = testee.createDynamicRepositoryClass(String.class, Long.class);
        Class<JpaRepository<String, Long>> dynamicRepositoryClass2 = testee.createDynamicRepositoryClass(String.class, Long.class);

        assertArrayEquals(dynamicRepositoryClass1.getGenericInterfaces(), dynamicRepositoryClass2.getGenericInterfaces());
    }

}