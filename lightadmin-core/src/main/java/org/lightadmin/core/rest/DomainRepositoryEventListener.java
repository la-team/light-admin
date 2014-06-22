package org.lightadmin.core.rest;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

import javax.annotation.PostConstruct;
import java.io.IOException;

public class DomainRepositoryEventListener extends AbstractRepositoryEventListener<Object> {

    @Autowired
    private GlobalAdministrationConfiguration configuration;

    @Autowired
    private LightAdminConfiguration lightAdminConfiguration;

    @Autowired
    private Repositories repositories;

    private OperationBuilder operationBuilder;

    @PostConstruct
    public void init() {
        this.operationBuilder = OperationBuilder.operationBuilder(configuration, lightAdminConfiguration);
    }

    @Override
    protected void onAfterSave(final Object entity) {
        PersistentEntity<?, ?> persistentEntity = repositories.getPersistentEntity(entity.getClass());

        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                if (PersistentPropertyType.isOfFileReferenceType(property)) {
                    try {
                        operationBuilder.saveOperation(entity).performCleanup(property);
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    @Override
    protected void onBeforeDelete(final Object entity) {
        PersistentEntity<?, ?> persistentEntity = repositories.getPersistentEntity(entity.getClass());

        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                if (PersistentPropertyType.isOfFileReferenceType(property)) {
                    operationBuilder.deleteOperation(entity).perform(property);
                }
            }
        });
    }
}