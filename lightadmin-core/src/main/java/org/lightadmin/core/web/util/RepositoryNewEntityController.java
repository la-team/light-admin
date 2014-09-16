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
package org.lightadmin.core.web.util;

import org.springframework.beans.BeanUtils;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Constructor;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.ClassUtils.getConstructorIfAvailable;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
@RepositoryRestController
public class RepositoryNewEntityController {

    private static final String BASE_MAPPING = "/{repository}";

    @RequestMapping(value = BASE_MAPPING + "/new", method = GET)
    public ResponseEntity<Resource<?>> getItemResource(RootResourceInformation resourceInformation, PersistentEntityResourceAssembler assembler)
            throws HttpRequestMethodNotSupportedException {

        Constructor<?> constructor = getConstructorIfAvailable(resourceInformation.getDomainType());
        if (constructor == null) {
            return new ResponseEntity<Resource<?>>(NOT_IMPLEMENTED);
        }

        Object domainObj = BeanUtils.instantiateClass(constructor);

        return new ResponseEntity<Resource<?>>(assembler.toResource(domainObj), OK);
    }
}