package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public interface DomainTypePropertyValidator {

	boolean isInvalidProperty( String propertyName, DomainTypeEntityMetadata entityMetadata );

	boolean isValidProperty( String propertyName, DomainTypeEntityMetadata entityMetadata );
}