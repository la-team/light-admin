package org.springframework.data.rest.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RepositoryRestController
public class RepositoryMetadataController {

    private static final String BASE_MAPPING = "/{repository}";

    private final DomainTypeToJsonMetadataConverter domainTypeToJsonMetadataConverter;

    @Autowired
    public RepositoryMetadataController(DomainTypeToJsonMetadataConverter domainTypeToJsonMetadataConverter) {
        this.domainTypeToJsonMetadataConverter = domainTypeToJsonMetadataConverter;
    }

    @RequestMapping(value = BASE_MAPPING + "/metadata", method = GET)
    public HttpEntity<JsonConfigurationMetadata> schema(RootResourceInformation resourceInformation) {
        JsonConfigurationMetadata jsonConfigurationMetadata = domainTypeToJsonMetadataConverter.convert(resourceInformation.getPersistentEntity());

        return new ResponseEntity<>(jsonConfigurationMetadata, HttpStatus.OK);
    }
}