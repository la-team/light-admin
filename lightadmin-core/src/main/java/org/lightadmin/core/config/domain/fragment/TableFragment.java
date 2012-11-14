package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.field.FieldMetadata;

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
}