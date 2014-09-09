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

import org.lightadmin.core.storage.FileResourceStorage;
import org.lightadmin.core.web.support.DynamicRepositoryEntityLinks;
import org.lightadmin.core.web.support.FilePropertyValue;
import org.lightadmin.core.web.support.FileResourceLoader;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.rest.webmvc.support.BackendId;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.isOfFileType;
import static org.springframework.data.rest.webmvc.ControllerUtils.toEmptyResponse;
import static org.springframework.data.rest.webmvc.ControllerUtils.toResponseEntity;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RepositoryRestController
public class RepositoryFilePropertyController {

    private static final String BASE_MAPPING = "/{repository}/{id}/{property}";

    private BeanFactory beanFactory;

    @Autowired
    public RepositoryFilePropertyController(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @RequestMapping(value = BASE_MAPPING + "/file", method = GET)
    public void filePropertyOfEntity(RootResourceInformation repoRequest, ServletResponse response, @BackendId Serializable id, @PathVariable String property) throws Exception {
        PersistentEntity<?, ?> persistentEntity = repoRequest.getPersistentEntity();
        RepositoryInvoker invoker = repoRequest.getInvoker();

        Object domainObj = invoker.invokeFindOne(id);

        if (null == domainObj) {
            throw new ResourceNotFoundException();
        }

        PersistentProperty<?> prop = persistentEntity.getPersistentProperty(property);
        if (null == prop) {
            throw new ResourceNotFoundException();
        }

        if (isOfFileType(prop)) {
            fileResourceLoader().downloadFile(domainObj, prop, (HttpServletResponse) response);
        }
    }

    @RequestMapping(value = BASE_MAPPING + "/binary", method = GET)
    public ResponseEntity<?> filePropertyValueOfEntity(RootResourceInformation repoRequest, @BackendId Serializable id, @PathVariable String property) throws Exception {
        PersistentEntity<?, ?> persistentEntity = repoRequest.getPersistentEntity();
        RepositoryInvoker invoker = repoRequest.getInvoker();

        Object domainObj = invoker.invokeFindOne(id);

        if (null == domainObj) {
            throw new ResourceNotFoundException();
        }

        PersistentProperty<?> prop = persistentEntity.getPersistentProperty(property);
        if (null == prop) {
            throw new ResourceNotFoundException();
        }

        if (isOfFileType(prop)) {
            return toResponseEntity(OK, new HttpHeaders(), new Resource<FilePropertyValue>(evaluateFilePropertyValue(domainObj, prop)));
        }

        return toEmptyResponse(METHOD_NOT_ALLOWED);
    }

    @RequestMapping(value = BASE_MAPPING + "/file", method = DELETE)
    public ResponseEntity<?> deleteFileOfPropertyOfEntity(RootResourceInformation repoRequest, @BackendId Serializable id, @PathVariable String property) throws Exception {
        PersistentEntity<?, ?> persistentEntity = repoRequest.getPersistentEntity();
        RepositoryInvoker invoker = repoRequest.getInvoker();

        Object domainObj = invoker.invokeFindOne(id);

        if (null == domainObj) {
            throw new ResourceNotFoundException();
        }

        PersistentProperty<?> prop = persistentEntity.getPersistentProperty(property);
        if (null == prop) {
            throw new ResourceNotFoundException();
        }

        if (!isOfFileType(prop)) {
            return toEmptyResponse(METHOD_NOT_ALLOWED);
        }

        fileResourceStorage().delete(domainObj, prop);

        invoker.invokeSave(domainObj);

        return toEmptyResponse(OK);
    }

    @RequestMapping(value = BASE_MAPPING + "/file", method = {POST})
    public ResponseEntity<? extends ResourceSupport> saveFilePropertyOfEntity(final ServletServerHttpRequest request) throws Exception {
        final MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request.getServletRequest();

        final Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();

        if (!fileMap.isEmpty()) {
            final Map.Entry<String, MultipartFile> fileEntry = fileMap.entrySet().iterator().next();

            return toResponseEntity(OK, new HttpHeaders(), fileResource(fileEntry));
        }

        return toEmptyResponse(METHOD_NOT_ALLOWED);
    }

    private FilePropertyValue evaluateFilePropertyValue(Object instance, PersistentProperty persistentProperty) {
        FileResourceStorage fileResourceStorage = fileResourceStorage();

        try {
            if (!fileResourceStorage.fileExists(instance, persistentProperty)) {
                return new FilePropertyValue(false);
            }

            Link fileLink = entityLinks().linkForFilePropertyLink(instance, persistentProperty);

            byte[] fileData = fileResourceStorage.load(instance, persistentProperty);

            return new FilePropertyValue(fileLink, fileData);

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    private Resource<FilePropertyValue> fileResource(Map.Entry<String, MultipartFile> fileEntry) throws IOException {
        MultipartFile multipartFile = fileEntry.getValue();

        FilePropertyValue filePropertyValue = new FilePropertyValue(multipartFile.getOriginalFilename(), multipartFile.getBytes());

        return new Resource<FilePropertyValue>(filePropertyValue);
    }

    private FileResourceLoader fileResourceLoader() {
        return beanFactory.getBean(FileResourceLoader.class);
    }

    private FileResourceStorage fileResourceStorage() {
        return beanFactory.getBean(FileResourceStorage.class);
    }

    private DynamicRepositoryEntityLinks entityLinks() {
        return beanFactory.getBean(DynamicRepositoryEntityLinks.class);
    }
}