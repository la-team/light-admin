/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.persistence.repository;

import javassist.ClassPool;
import javassist.CtClass;
import org.lightadmin.core.util.DynamicRepositoryBeanNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.UUID;

import static javassist.bytecode.SignatureAttribute.*;
import static org.apache.commons.lang3.ArrayUtils.toArray;

public class JavassistDynamicJpaRepositoryClassFactory implements DynamicRepositoryClassFactory {

    private final Logger logger = LoggerFactory.getLogger(JavassistDynamicJpaRepositoryClassFactory.class);

    private final DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator;
    private final ClassLoader classLoader;

    public JavassistDynamicJpaRepositoryClassFactory(DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator) {
        this.dynamicRepositoryBeanNameGenerator = dynamicRepositoryBeanNameGenerator;
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, ID extends Serializable> Class<DynamicJpaRepository<T, ID>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType) {
        try {
            ClassPool classPool = ClassPool.getDefault();

            CtClass baseInterface = classPool.getOrNull(DynamicJpaRepository.class.getName());
            if (baseInterface == null) {
                baseInterface = classPool.makeInterface(DynamicJpaRepository.class.getName());
            }

            CtClass dynamicRepositoryInterface = classPool.makeInterface(generateDynamicRepositoryClassReference(domainType), baseInterface);

            ClassType baseInterfaceType = classType(DynamicJpaRepository.class, toArray(typeArgument(domainType), typeArgument(idType)));

            dynamicRepositoryInterface.setGenericSignature(classSignature(baseInterfaceType).encode());

            return dynamicRepositoryInterface.toClass(classLoader, JavassistDynamicJpaRepositoryClassFactory.class.getProtectionDomain());
        } catch (Exception e) {
            logger.error("Problem occured during DynamicRepository class creation process", e);
            throw new RuntimeException(e);
        }
    }

    private ClassSignature classSignature(ClassType baseInterfaceType) {
        return new ClassSignature(null, null, new ClassType[]{baseInterfaceType});
    }

    private <T, ID extends Serializable> ClassType classType(Class<DynamicJpaRepository> classType, TypeArgument[] typeArguments) {
        return new ClassType(classType.getName(), typeArguments);
    }

    private <T> TypeArgument typeArgument(Class<T> domainType) {
        return new TypeArgument(new ClassType(domainType.getName()));
    }

    private String generateDynamicRepositoryClassReference(Class<?> domainType) {
        String uuid = StringUtils.deleteAny(UUID.randomUUID().toString(), "-");
        String packageName = ClassUtils.getPackageName(JavassistDynamicJpaRepositoryClassFactory.class);
        String domainRepositoryClassName = dynamicRepositoryBeanNameGenerator.generateBeanName(domainType, DynamicJpaRepository.class);

        return packageName + "." + domainRepositoryClassName + "$$DYNAMIC$$" + uuid;
    }
}