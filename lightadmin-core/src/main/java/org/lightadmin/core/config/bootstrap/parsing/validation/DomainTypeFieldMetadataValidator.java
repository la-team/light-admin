package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.util.Assert;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

class DomainTypeFieldMetadataValidator implements FieldMetadataValidator<FieldMetadata> {

	private final Map<Class<? extends FieldMetadata>, FieldMetadataValidator<? extends FieldMetadata>> fieldMetadataValidators = newHashMap();

	public DomainTypeFieldMetadataValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		fieldMetadataValidators.put( PersistentFieldMetadata.class, new PersistentFieldMetadataValidator( entityMetadataResolver ) );
		fieldMetadataValidators.put( TransientFieldMetadata.class, new TransientFieldMetadataValidator() );
		fieldMetadataValidators.put( CustomFieldMetadata.class, new CustomFieldMetadataValidator() );
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean isValidFieldMetadata( final FieldMetadata fieldMetadata, final Class<?> domainType ) {
		final FieldMetadataValidator fieldMetadataValidator = fieldMetadataValidators.get( fieldMetadata.getClass() );

		Assert.notNull( fieldMetadataValidator );

		return fieldMetadataValidator.isValidFieldMetadata( fieldMetadata, domainType );
	}
}