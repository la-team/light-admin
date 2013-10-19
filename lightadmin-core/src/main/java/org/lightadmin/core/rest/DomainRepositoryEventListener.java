package org.lightadmin.core.rest;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.data.rest.repository.RepositoryMetadata;
import org.springframework.data.rest.repository.context.AbstractRepositoryEventListener;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;

import static org.lightadmin.core.persistence.metamodel.AttributeMetadataUtils.fileReferenceAttributeMetadatas;
import static org.lightadmin.core.rest.binary.OperationBuilder.operationBuilder;

@SuppressWarnings("unchecked")
public class DomainRepositoryEventListener extends AbstractRepositoryEventListener {

    @Autowired
    private GlobalAdministrationConfiguration configuration;

    @Autowired
    private WebContext webContext;

    private OperationBuilder operationBuilder;

    @PostConstruct
    public void init() {
        this.operationBuilder = operationBuilder(configuration, webContext);
    }

    @Override
    protected void onAfterSave(Object entity) {
        final RepositoryMetadata repoMeta = repositoryMetadataFor(entity.getClass());
        final Collection<AttributeMetadata> embeddedAttributeMetadatas = (Collection<AttributeMetadata>) repoMeta.entityMetadata().embeddedAttributes().values();
        final Collection<AttributeMetadata> fileReferenceAttributeMetadatas = fileReferenceAttributeMetadatas(embeddedAttributeMetadatas);

        for (AttributeMetadata fileReferenceAttrMetadata : fileReferenceAttributeMetadatas) {
            try {
                operationBuilder.saveOperation(entity).performCleanup(fileReferenceAttrMetadata);
            } catch (IOException e) {
            }
        }
    }

    @Override
    protected void onAfterDelete(Object entity) {
        final RepositoryMetadata repoMeta = repositoryMetadataFor(entity.getClass());
        final Collection<AttributeMetadata> embeddedAttributeMetadatas = (Collection<AttributeMetadata>) repoMeta.entityMetadata().embeddedAttributes().values();
        final Collection<AttributeMetadata> fileReferenceAttributeMetadatas = fileReferenceAttributeMetadatas(embeddedAttributeMetadatas);

        for (AttributeMetadata fileReferenceAttrMetadata : fileReferenceAttributeMetadatas) {
            operationBuilder.deleteOperation(entity).perform(fileReferenceAttrMetadata);
        }
    }
}