package org.springframework.data.rest.webmvc;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.hateoas.ResourceProcessor;

import static org.lightadmin.core.web.util.ApplicationUrlResolver.selfDomainLink;

public class DynamicPersistentEntityResourceProcessor implements ResourceProcessor<PersistentEntityResource<?>> {

    private GlobalAdministrationConfiguration adminConfiguration;

    public DynamicPersistentEntityResourceProcessor(GlobalAdministrationConfiguration adminConfiguration) {
        this.adminConfiguration = adminConfiguration;
    }

    @Override
    public PersistentEntityResource<?> process(PersistentEntityResource<?> resource) {
        DomainTypeBasicConfiguration domainTypeBasicConfiguration = adminConfiguration.forDomainType(resource.getPersistentEntity().getType());
        resource.add(selfDomainLink(resource, domainTypeBasicConfiguration));
        return resource;
    }
}