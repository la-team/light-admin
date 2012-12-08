package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.Persistable;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataAware;

import java.util.Set;

import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.*;

public class FragmentListViewConfigurationUnit extends DomainTypeConfigurationUnit implements ListViewConfigurationUnit, DomainTypeEntityMetadataAware {

	private Fragment fragment;

	FragmentListViewConfigurationUnit( Class<?> domainType, final Fragment fragment ) {
		super( domainType );

		this.fragment = fragment;
	}

	@Override
	public Fragment getFragment() {
		return fragment;
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.LIST_VIEW;
	}

	@Override
	public void setDomainTypeEntityMetadata( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		final Set<FieldMetadata> fields = this.fragment.getFields();

		if ( containsPersistentField( fields, domainTypeEntityMetadata.getIdAttribute().getName() ) ) {
			final PersistentFieldMetadata primaryKeyField = getPersistentField( fields, domainTypeEntityMetadata.getIdAttribute().getName() );
			primaryKeyField.setPrimaryKey( true );
		} else {
			this.fragment = new TableFragment( addPrimaryKeyPersistentField( fields, domainTypeEntityMetadata.getIdAttribute() ) );
		}

		for (FieldMetadata field : this.fragment.getFields()) {
			if (field instanceof DomainTypeAttributeMetadataAware && field instanceof Persistable) {
				((DomainTypeAttributeMetadataAware) field).setAttributeMetadata(domainTypeEntityMetadata.getAttribute(((Persistable) field).getField()));
			}
		}

	}
}
