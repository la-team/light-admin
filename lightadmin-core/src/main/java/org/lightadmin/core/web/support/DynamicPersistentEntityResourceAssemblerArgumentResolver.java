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
package org.lightadmin.core.web.support;

import org.springframework.core.MethodParameter;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.config.PersistentEntityResourceAssemblerArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class DynamicPersistentEntityResourceAssemblerArgumentResolver implements HandlerMethodArgumentResolver {

    private PersistentEntityResourceAssemblerArgumentResolver delegate;

    public DynamicPersistentEntityResourceAssemblerArgumentResolver(PersistentEntityResourceAssemblerArgumentResolver delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return this.delegate.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PersistentEntityResourceAssembler persistentEntityResourceAssembler = (PersistentEntityResourceAssembler) delegate.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        return new DynamicPersistentEntityResourceAssembler(persistentEntityResourceAssembler);
    }
}