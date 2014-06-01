package org.lightadmin.core.rest;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

public class DomainTypeResourceSupport {

    protected final RepositoryRestConfiguration restConfiguration;

    public DomainTypeResourceSupport(RepositoryRestConfiguration restConfiguration) {
        this.restConfiguration = restConfiguration;
    }
}
