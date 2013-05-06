package org.lightadmin.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.test.model.TestOrder;

@SuppressWarnings( "unused" )
@Administration( TestOrder.class )
public class InvalidFieldAdmin {

	public static FiltersConfigurationUnit filters( final FiltersConfigurationUnitBuilder filterBuilder ) {
		return filterBuilder
				.filter( "Missing Field", "missingField" ).build();
	}
}
