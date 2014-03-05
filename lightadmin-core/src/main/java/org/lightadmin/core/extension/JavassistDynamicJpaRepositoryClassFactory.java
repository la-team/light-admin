package org.lightadmin.core.extension;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.UUID;

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
    public <T, ID extends Serializable> Class<JpaRepository<T, ID>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType) {
        try {
            ClassPool classPool = ClassPool.getDefault();

            CtClass jpaRepositoryCtClass = classPool.getOrNull(JpaRepository.class.getName());
            if (jpaRepositoryCtClass == null) {
                jpaRepositoryCtClass = classPool.makeInterface(JpaRepository.class.getName());
            }

            CtClass dynamicRepositoryInterface = classPool.makeInterface(generateDynamicRepositoryClassReference(domainType), jpaRepositoryCtClass);
            ClassFile dynamicRepositoryInterfaceClassFile = dynamicRepositoryInterface.getClassFile();

            ConstPool classFileConstPool = dynamicRepositoryInterfaceClassFile.getConstPool();

            Annotation annot = new Annotation(RepositoryDefinition.class.getName(), classFileConstPool);
            annot.addMemberValue("domainClass", new ClassMemberValue(domainType.getName(), dynamicRepositoryInterfaceClassFile.getConstPool()));
            annot.addMemberValue("idClass", new ClassMemberValue(idType.getName(), dynamicRepositoryInterfaceClassFile.getConstPool()));

            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(classFileConstPool, AnnotationsAttribute.visibleTag);
            annotationsAttribute.addAnnotation(annot);
            dynamicRepositoryInterfaceClassFile.addAttribute(annotationsAttribute);

            return dynamicRepositoryInterface.toClass(classLoader, JavassistDynamicJpaRepositoryClassFactory.class.getProtectionDomain());
        } catch (Exception e) {
            logger.error("Problem occured during DynamicRepository class creation process", e);
            throw new RuntimeException(e);
        }
    }

    private String generateDynamicRepositoryClassReference(Class<?> domainType) {
        String uuid = StringUtils.deleteAny(UUID.randomUUID().toString(), "-");
        String packageName = ClassUtils.getPackageName(JavassistDynamicJpaRepositoryClassFactory.class);
        String domainRepositoryClassName = dynamicRepositoryBeanNameGenerator.generateBeanName(domainType, JpaRepository.class);

        return packageName + "." + domainRepositoryClassName + "$$DYNAMIC$$" + uuid;
    }
}