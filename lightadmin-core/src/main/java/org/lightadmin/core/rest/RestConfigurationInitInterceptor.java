package org.lightadmin.core.rest;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class RestConfigurationInitInterceptor implements WebRequestInterceptor {

    private final RepositoryRestConfiguration config;

    public RestConfigurationInitInterceptor(RepositoryRestConfiguration restConfiguration) {
        this.config = restConfiguration;
    }

    @Override
    public void preHandle(WebRequest webRequest) throws Exception {
        if (config.getBaseUri() == null) {
            HttpServletRequest request = (HttpServletRequest) webRequest.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            URI baseUri = ServletUriComponentsBuilder.fromServletMapping(request).pathSegment("rest").build().toUri();
            config.setBaseUri(baseUri);
        }
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
    }

}
