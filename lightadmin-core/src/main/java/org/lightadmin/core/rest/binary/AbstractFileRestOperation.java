package org.lightadmin.core.rest.binary;

import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.Serializable;

import static java.lang.String.valueOf;
import static org.apache.commons.io.FileUtils.getFile;
import static org.lightadmin.core.rest.binary.FileStorageUtils.relativePathToDomainStorageAttributeDirectory;
import static org.lightadmin.core.rest.binary.FileStorageUtils.relativePathToDomainStorageDirectory;

public abstract class AbstractFileRestOperation {

    protected final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration;
    protected final DomainTypeEntityMetadata domainTypeEntityMetadata;

    protected final LightAdminConfiguration lightAdminConfiguration;

    protected final Object entity;

    protected AbstractFileRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        this.domainTypeAdministrationConfiguration = configuration.forManagedDomainType(ClassUtils.getUserClass(entity));
        this.domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();

        this.lightAdminConfiguration = lightAdminConfiguration;

        this.entity = entity;
    }

    protected void resetAttrValue(AttributeMetadata attributeMetadata) {
        performDirectSave(attributeMetadata, null);
    }

    protected void performDirectSave(AttributeMetadata attrMeta, byte[] incomingVal) {
        attrMeta.set(incomingVal, entity);
    }

    protected File referencedFileDomainEntityAttributeDirectory(AttributeMetadata attrMeta) {
        final FileReference fileReference = attrMeta.annotation(FileReference.class);
        return getFile(fileReference.baseDirectory(), relativePathToDomainStorageAttributeDirectory(domainTypeName(), idAttributeValue(), attrMeta));
    }

    protected File fileStorageDomainEntityAttributeDirectory(AttributeMetadata attrMeta) {
        return getFile(lightAdminConfiguration.getFileStorageDirectory(), relativePathToDomainStorageAttributeDirectory(domainTypeName(), idAttributeValue(), attrMeta));
    }

    protected File referencedFileDomainEntityDirectory(AttributeMetadata attrMeta) {
        final FileReference fileReference = attrMeta.annotation(FileReference.class);
        return getFile(fileReference.baseDirectory(), relativePathToDomainStorageDirectory(domainTypeName(), idAttributeValue()));
    }

    protected File fileStorageDomainEntityDirectory() {
        return getFile(lightAdminConfiguration.getFileStorageDirectory(), relativePathToDomainStorageDirectory(domainTypeName(), idAttributeValue()));
    }

    protected File referencedFile(AttributeMetadata attrMeta) {
        final FileReference fileReference = attrMeta.annotation(FileReference.class);
        return getFile(fileReference.baseDirectory(), valueOf(attrMeta.get(entity)));
    }

    protected File fileStorageFile(AttributeMetadata attributeMetadata) {
        return getFile(lightAdminConfiguration.getFileStorageDirectory(), valueOf(attributeMetadata.get(entity)));
    }

    protected DynamicJpaRepository repository() {
        return domainTypeAdministrationConfiguration.getRepository();
    }

    protected Serializable idAttributeValue() {
        return (Serializable) domainTypeEntityMetadata.getIdAttribute().getValue(entity);
    }

    protected String domainTypeName() {
        return domainTypeAdministrationConfiguration.getDomainTypeName();
    }
}