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
package org.lightadmin.core.config.bootstrap;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.util.HashSet;
import java.util.Set;

public class JpaMetamodelMappingContextFactoryBean extends AbstractFactoryBean<JpaMetamodelMappingContext> {

    private EntityManager entityManager;

    @Override
    public Class<?> getObjectType() {
        return JpaMetamodelMappingContext.class;
    }

    @Override
    protected JpaMetamodelMappingContext createInstance() throws Exception {
        Metamodel metamodel = entityManager.getMetamodel();

        Set<ManagedType<?>> managedTypes = metamodel.getManagedTypes();
        Set<Class<?>> entitySources = new HashSet<Class<?>>(managedTypes.size());

        for (ManagedType<?> type : managedTypes) {

            Class<?> javaType = type.getJavaType();

            if (javaType != null) {
                entitySources.add(javaType);
            }
        }

        JpaMetamodelMappingContext context = new JpaMetamodelMappingContext(metamodel);
        context.setInitialEntitySet(entitySources);
        context.initialize();

        return context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        super.afterPropertiesSet();
    }

    @SuppressWarnings("unused")
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        this.entityManager = entityManager;
    }
}