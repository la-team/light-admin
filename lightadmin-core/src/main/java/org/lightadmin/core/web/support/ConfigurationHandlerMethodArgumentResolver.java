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

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.config.ResourceMetadataHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ConfigurationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private GlobalAdministrationConfiguration configuration;
    private ResourceMetadataHandlerMethodArgumentResolver resourceMetadataResolver;

    public ConfigurationHandlerMethodArgumentResolver(GlobalAdministrationConfiguration configuration, ResourceMetadataHandlerMethodArgumentResolver resourceMetadataResolver) {
        this.configuration = configuration;
        this.resourceMetadataResolver = resourceMetadataResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DomainTypeAdministrationConfiguration.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ResourceMetadata resourceMetadata = resourceMetadataResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        return configuration.forManagedDomainType(resourceMetadata.getDomainType());
    }
}