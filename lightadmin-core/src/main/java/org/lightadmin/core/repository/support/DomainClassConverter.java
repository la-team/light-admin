package org.lightadmin.core.repository.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

public class DomainClassConverter implements ConditionalGenericConverter {

	private final ConversionService conversionService;
	private final EntityManager entityManager;

	public DomainClassConverter(ConversionService conversionService, EntityManager entityManager ) {
		this.conversionService = conversionService;
		this.entityManager = entityManager;
	}

	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton( new ConvertiblePair( Object.class, Object.class ) );
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		Serializable id = conversionService.convert(source, idType( targetType ) );
		return entityManager.find( targetType.getType(), id );
	}

	private Class<? extends Serializable> idType( final TypeDescriptor targetType ) {
		final JpaEntityInformation<?,?> entityInformation = JpaEntityInformationSupport.getMetadata( targetType.getType(), entityManager );
		return entityInformation.getIdType();
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return conversionService.canConvert(sourceType.getType(), idType( targetType ));
	}
}