package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.File;
import java.io.Serializable;

import static java.lang.String.valueOf;
import static org.apache.commons.io.FileUtils.getFile;
import static org.lightadmin.core.rest.binary.FileStorageUtils.relativePathToStoreBinaryAttrValue;
import static org.springframework.util.ClassUtils.isAssignable;

public abstract class AbstractFileRestOperation {

    protected final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration;
    protected final DomainTypeEntityMetadata domainTypeEntityMetadata;

    protected final WebContext webContext;

    protected final Object entity;

    protected AbstractFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        this.domainTypeAdministrationConfiguration = configuration.forManagedDomainType(entity.getClass());
        this.domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();

        this.webContext = webContext;

        this.entity = entity;
    }

    protected void resetAttrValue(AttributeMetadata attributeMetadata) {
        performDirectSave(attributeMetadata, null);
    }

    protected void resetAttrValue(DomainTypeAttributeMetadata attributeMetadata) {
        performDirectSave(attributeMetadata, null);
    }

    protected void performDirectSave(AttributeMetadata attrMeta, byte[] incomingVal) {
        attrMeta.set(incomingVal, entity);
    }

    protected void performDirectSave(DomainTypeAttributeMetadata attrMeta, byte[] incomingVal) {
        attrMeta.setValue(incomingVal, entity);
    }

    protected File referencedFile(FileReference fileReference) {
        return getFile(fileReference.baseDirectory(), valueOf(referenceDomainAttribute(fileReference).getValue(entity)));
    }

    protected File fileStorageFile(AttributeMetadata attributeMetadata) {
        return getFile(webContext.getFileStorageDirectory(), relativePathToStoreBinaryAttrValue(domainTypeName(), idAttributeValue(), attributeMetadata));
    }

    protected boolean isOfStringType(DomainTypeAttributeMetadata domainTypeAttributeMetadata) {
        return isAssignable(String.class, domainTypeAttributeMetadata.getType());
    }

    protected boolean fileStorageModeDisabled() {
        return !webContext.isFileStorageEnabled();
    }

    protected DynamicJpaRepository repository() {
        return domainTypeAdministrationConfiguration.getRepository();
    }

    protected DomainTypeAttributeMetadata referenceDomainAttribute(FileReference fileReference) {
        return domainTypeEntityMetadata.getAttribute(fileReference.field());
    }

    protected Serializable idAttributeValue() {
        return (Serializable) domainTypeEntityMetadata.getIdAttribute().getValue(entity);
    }

    protected String domainTypeName() {
        return domainTypeAdministrationConfiguration.getDomainTypeName();
    }
}