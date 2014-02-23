package org.lightadmin.core.extension;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.asm.Type;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.proxy.InterfaceMaker;

import java.io.Serializable;

public class CGLibDynamicJpaRepositoryClassFactory implements DynamicJpaRepositoryClassFactory {

    private final DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator;

    public CGLibDynamicJpaRepositoryClassFactory(DynamicRepositoryBeanNameGenerator dynamicRepositoryBeanNameGenerator) {
        this.dynamicRepositoryBeanNameGenerator = dynamicRepositoryBeanNameGenerator;
    }

    @Override
    public <T, ID extends Serializable> Class<? extends DynamicJpaRepository<?, ? extends Serializable>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType) {
        Signature signature = new Signature("foo", Type.DOUBLE_TYPE, new Type[]{Type.INT_TYPE});
        InterfaceMaker interfaceMaker = new InterfaceMaker();
        interfaceMaker.add(signature, new Type[0]);
        Class iface = interfaceMaker.create();

        return iface;
    }
}