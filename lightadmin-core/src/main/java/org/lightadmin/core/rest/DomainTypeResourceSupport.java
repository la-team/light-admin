package org.lightadmin.core.rest;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class DomainTypeResourceSupport {

    protected final RepositoryRestConfiguration restConfiguration;

    public DomainTypeResourceSupport(RepositoryRestConfiguration restConfiguration) {
        this.restConfiguration = restConfiguration;
    }

    public Link selfLink(String domainTypeName, Object id) {
        URI selfUri = UriComponentsBuilder.fromUri(restConfiguration.getBaseUri())
                .pathSegment(domainTypeName)
                .pathSegment(id.toString()).build().toUri();

        return new Link(selfUri.toString(), "self");
    }

    public Link selfLink(DomainTypeBasicConfiguration domainTypeConfig, Object id) {
        return selfLink(domainTypeConfig.getPluralDomainTypeName(), id);
    }

}
