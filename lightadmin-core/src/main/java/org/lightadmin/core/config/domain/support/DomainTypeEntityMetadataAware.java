package org.lightadmin.core.config.domain.support;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public interface DomainTypeEntityMetadataAware {

	void setDomainTypeEntityMetadata( DomainTypeEntityMetadata domainTypeEntityMetadata );
}