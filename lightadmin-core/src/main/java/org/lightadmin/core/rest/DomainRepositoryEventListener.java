package org.lightadmin.core.rest;

//import org.springframework.data.rest.repository.RepositoryMetadata;
//import org.springframework.data.rest.repository.context.AbstractRepositoryEventListener;

@SuppressWarnings("unchecked")
public class DomainRepositoryEventListener {
//        extends AbstractRepositoryEventListener {
//
//    @Autowired
//    private GlobalAdministrationConfiguration configuration;
//
//    @Autowired
//    private LightAdminConfiguration lightAdminConfiguration;
//
//    private OperationBuilder operationBuilder;
//
//    @PostConstruct
//    public void init() {
//        this.operationBuilder = operationBuilder(configuration, lightAdminConfiguration);
//    }
//
//    @Override
//    protected void onAfterSave(Object entity) {
//        final RepositoryMetadata repoMeta = repositoryMetadataFor(entity.getClass());
//        final Collection<AttributeMetadata> embeddedAttributeMetadatas = (Collection<AttributeMetadata>) repoMeta.entityMetadata().embeddedAttributes().values();
//        final Collection<AttributeMetadata> fileReferenceAttributeMetadatas = fileReferenceAttributeMetadatas(embeddedAttributeMetadatas);
//
//        for (AttributeMetadata fileReferenceAttrMetadata : fileReferenceAttributeMetadatas) {
//            try {
//                operationBuilder.saveOperation(entity).performCleanup(fileReferenceAttrMetadata);
//            } catch (IOException e) {
//            }
//        }
//    }
//
//    @Override
//    protected void onBeforeDelete(Object entity) {
//        final RepositoryMetadata repoMeta = repositoryMetadataFor(entity.getClass());
//        final Collection<AttributeMetadata> embeddedAttributeMetadatas = (Collection<AttributeMetadata>) repoMeta.entityMetadata().embeddedAttributes().values();
//        final Collection<AttributeMetadata> fileReferenceAttributeMetadatas = fileReferenceAttributeMetadatas(embeddedAttributeMetadatas);
//
//        for (AttributeMetadata fileReferenceAttrMetadata : fileReferenceAttributeMetadatas) {
//            operationBuilder.deleteOperation(entity).perform(fileReferenceAttrMetadata);
//        }
//    }
}