package org.lightadmin.core.config.domain.show;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataAware;

import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.*;

public class DefaultShowViewConfigurationUnit extends DomainTypeConfigurationUnit implements ShowViewConfigurationUnit, DomainTypeEntityMetadataAware {

	private Set<FieldMetadata> fields = newLinkedHashSet();

	protected DefaultShowViewConfigurationUnit( final Class<?> domainType ) {
		super( domainType );
	}

	public void addField( FieldMetadata fieldMetadata ) {
		fields.add( fieldMetadata );
	}

	public Set<FieldMetadata> getFields() {
		return newLinkedHashSet( fields );
	}

	@Override
	public Iterator<FieldMetadata> iterator() {
		return getFields().iterator();
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.SHOW_VIEW;
	}

	@Override
	public void setDomainTypeEntityMetadata( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		if ( containsPersistentField( fields, domainTypeEntityMetadata.getIdAttribute().getName() ) ) {
			final PersistentFieldMetadata primaryKeyField = getPersistentField( fields, domainTypeEntityMetadata.getIdAttribute().getName() );
			primaryKeyField.setPrimaryKey( true );
			return;
		}

		fields = addPrimaryKeyPersistentField( fields, domainTypeEntityMetadata.getIdAttribute() );
	}
}