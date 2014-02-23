package org.lightadmin.core.extension;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.util.ClassUtils;

import java.io.Serializable;

public class JavassistDynamicJpaRepositoryClassFactory implements DynamicJpaRepositoryClassFactory {

    private final Logger logger = LoggerFactory.getLogger(JavassistDynamicJpaRepositoryClassFactory.class);

    private final DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator;

    public JavassistDynamicJpaRepositoryClassFactory(DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator) {
        this.dynamicRepositoryBeanNameGenerator = dynamicRepositoryBeanNameGenerator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, ID extends Serializable> Class<? extends DynamicJpaRepository<T, ID>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType) {
        try {
            ClassPool classPool = ClassPool.getDefault();

            CtClass superCtClass = classPool.makeInterface(DynamicJpaRepository.class.getName());
            CtClass ctClass = classPool.makeInterface(generateDynamicRepositoryClassReference(domainType), superCtClass);
            ClassFile classFile = ctClass.getClassFile();
            ConstPool classFileConstPool = classFile.getConstPool();

            Annotation annot = new Annotation(RepositoryDefinition.class.getName(), classFileConstPool);
            annot.addMemberValue("domainClass", new ClassMemberValue(domainType.getName(), classFile.getConstPool()));
            annot.addMemberValue("idClass", new ClassMemberValue(idType.getName(), classFile.getConstPool()));

            AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(classFileConstPool, AnnotationsAttribute.visibleTag);
            annotationsAttribute.addAnnotation(annot);
            classFile.addAttribute(annotationsAttribute);

//            SignatureAttribute.ClassSignature cs = new SignatureAttribute.ClassSignature(new SignatureAttribute.TypeArgument[] {
//                    new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("java.lang.Object", null)),
//                    new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("java.lang.Object", null))
//            });

//            SignatureAttribute.ClassType cs = new SignatureAttribute.ClassType(generateDynamicRepositoryClassReference(domainType), new SignatureAttribute.TypeArgument[] {
//                    new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("java.lang.String", null)),
//                    new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType("java.lang.Long", null))
//            });
//
//            classFile.addAttribute(new );

//            ctClass.setGenericSignature(cs.encode());    // &lt;T:Ljava/lang/Object;&gt;Ljava/lang/Object;

//            SignatureAttribute signatureAttribute = new SignatureAttribute(classFileConstPool,
//                    "()Ljava/util/List<Ljava/lang/String;>;");
//            classFile.addAttribute(signatureAttribute);

            return ctClass.toClass();
        } catch (Exception e) {
            logger.error("Problem occured during DynamicRepository class creation process", e);
            throw new RuntimeException(e);
        }
    }

    private String generateDynamicRepositoryClassReference(Class<?> domainType) {
        String packageName = ClassUtils.getPackageName(DynamicJpaRepository.class);
        String dynamicRepositoryClassName = dynamicRepositoryBeanNameGenerator.generateBeanName(domainType, DynamicJpaRepository.class);

        return packageName + "." + dynamicRepositoryClassName;
    }
}