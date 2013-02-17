package org.lightadmin.data;

import java.util.HashMap;
import java.util.Map;

public class FieldValueBuilder {

	private Map<String, String> fieldValueMap = new HashMap<String, String>();

	public FieldValueBuilder add( String fieldName, String fieldValue ) {
		fieldValueMap.put(fieldName,  fieldValue);

		return this;
	}

	public Map<String, String> build() {
		return fieldValueMap;
	}

}
