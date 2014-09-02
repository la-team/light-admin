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
package org.lightadmin.core.web;

import org.lightadmin.core.web.json.DomainTypeToJsonMetadataConverter;
import org.lightadmin.core.web.json.JsonConfigurationMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.OK;
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

        return new ResponseEntity<JsonConfigurationMetadata>(jsonConfigurationMetadata, OK);
    }
}