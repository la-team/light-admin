package org.lightadmin.core.web.util;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.springframework.data.rest.repository.AttributeMetadata;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.lightadmin.core.util.ResponseUtils.*;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@SuppressWarnings("unused")
public class FileResourceLoader {

    private final GlobalAdministrationConfiguration globalAdministrationConfiguration;

    private final OperationBuilder operationBuilder;

    public FileResourceLoader(GlobalAdministrationConfiguration globalAdministrationConfiguration, LightAdminConfiguration lightAdminConfiguration) {
        this.globalAdministrationConfiguration = globalAdministrationConfiguration;

        this.operationBuilder = OperationBuilder.operationBuilder(globalAdministrationConfiguration, lightAdminConfiguration);
    }

    public void downloadFile(Object entity, AttributeMetadata attrMeta, HttpServletResponse response) throws IOException {
        final long size = operationBuilder.getOperation(entity).performCopy(attrMeta, response.getOutputStream());
        final String eTag = eTag(entity.getClass(), attrMeta.name(), size);

        addImageResourceHeaders(response, octetStreamResponseHeader(APPLICATION_OCTET_STREAM, size, eTag));
    }

    public void downloadFile(Object entity, String field, HttpServletResponse response) throws IOException {
        Class<?> domainType = entity.getClass();

        downloadFile(entity, attributeMetadata(domainType, field), response);
    }

    private AttributeMetadata attributeMetadata(Class<?> domainType, String field) {
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = globalAdministrationConfiguration.forManagedDomainType(domainType);

        DomainTypeEntityMetadata domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();

        DomainTypeAttributeMetadata attribute = domainTypeEntityMetadata.getAttribute(field);

        return attribute.getAttributeMetadata();
    }
}