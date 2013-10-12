package org.lightadmin.core.web.util;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;
import org.springframework.data.rest.repository.jpa.JpaRepositoryMetadata;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;

public class FileResourceLoader {

    private final DynamicJpaRepositoryExporter jpaRepositoryExporter;
    private final WebContext webContext;

    private final ImageResourceControllerSupport imageResourceControllerSupport;

    private final OperationBuilder operationBuilder;

    public FileResourceLoader(GlobalAdministrationConfiguration globalAdministrationConfiguration, DynamicJpaRepositoryExporter jpaRepositoryExporter, WebContext webContext) {
        this.jpaRepositoryExporter = jpaRepositoryExporter;
        this.webContext = webContext;

        this.operationBuilder = OperationBuilder.operationBuilder(globalAdministrationConfiguration, webContext);
        this.imageResourceControllerSupport = new ImageResourceControllerSupport();
    }

    public ResponseEntity<?> loadFile(Object entity, AttributeMetadata attrMeta, HttpServletResponse response, int width, int height) throws IOException {
        if (webContext.isFileStreamingEnabled()) {
            operationBuilder.getOperation(entity).performCopy(attrMeta, response.getOutputStream());
            return new ResponseEntity(OK);
        }

        byte[] fileData = operationBuilder.getOperation(entity).perform(attrMeta);
        return imageResourceControllerSupport.downloadImageResource(fileData, width, height);
    }

    public ResponseEntity<?> loadFile(Object entity, String field, HttpServletResponse response, int width, int height) throws IOException {
        final AttributeMetadata attrMeta = attributeMetadata(entity.getClass(), field);

        return loadFile(entity, attrMeta, response, width, height);
    }

    private JpaAttributeMetadata attributeMetadata(Class<?> domainType, String field) {
        JpaRepositoryMetadata repositoryMetadata = jpaRepositoryExporter.repositoryMetadataFor(domainType);

        return repositoryMetadata.entityMetadata().attribute(field);
    }
}