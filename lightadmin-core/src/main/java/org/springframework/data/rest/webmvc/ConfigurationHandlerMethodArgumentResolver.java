package org.springframework.data.rest.webmvc;

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