package org.lightadmin.core.rest;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.net.URI;

import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

public class DomainTypeResourceSupport {

    protected final RepositoryRestConfiguration restConfiguration;

    public DomainTypeResourceSupport(RepositoryRestConfiguration restConfiguration) {
        this.restConfiguration = restConfiguration;
    }

    public Link selfDomainLink(String domainTypeName, Serializable id) {
        String baseRestUrl = restConfiguration.getBaseUri().toString();
        String baseUrl = substringBeforeLast(baseRestUrl, "/");
        URI selfUri = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment("domain")
                .pathSegment(domainTypeName)
                .pathSegment(id.toString()).build().toUri();

        return new Link(selfUri.toString(), "selfDomainLink");
    }

    public Link selfLink(String domainTypeName, Serializable id) {
        URI selfUri = UriComponentsBuilder.fromUri(restConfiguration.getBaseUri())
                .pathSegment(domainTypeName)
                .pathSegment(id.toString()).build().toUri();

        return new Link(selfUri.toString(), "self");
    }

    public Link selfLink(DomainTypeBasicConfiguration domainTypeConfig, Serializable id) {
        return selfLink(domainTypeConfig.getDomainTypeName(), id);
    }

}
