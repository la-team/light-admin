package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.beans.parsing.ConfigurationUnitPropertyFilter;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class TableFragment implements Fragment {

	private Set<FieldMetadata> fields = newLinkedHashSet();

	TableFragment() {
	}

	public void addField( FieldMetadata fieldMetadata ) {
		fields.add( fieldMetadata );
	}

	public Set<FieldMetadata> getFields() {
		return newLinkedHashSet( fields );
	}

	@Override
	public FieldMetadata getField( final String fieldName ) {
		for ( FieldMetadata field : fields ) {
			if ( field.getFieldName().equals( fieldName ) ) {
				return field;
			}
		}
		return null;
	}

	@Override
	public Fragment filter( final ConfigurationUnitPropertyFilter propertyFilter ) {
		TableFragment tableFragment = new TableFragment();
		for ( FieldMetadata field : fields ) {
			if ( propertyFilter.apply( field.getFieldName() ) ) {
				tableFragment.addField( field );
			}
		}
		return tableFragment;
	}
}