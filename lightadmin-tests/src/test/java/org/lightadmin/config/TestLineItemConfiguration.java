package org.lightadmin.config;

import org.lightadmin.core.config.annotation.Administration;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.test.model.TestLineItem;
import org.lightadmin.test.nameExtractor.LineItemNameExtractor;

@SuppressWarnings( "unused" )
@Administration( TestLineItem.class )
public class TestLineItemConfiguration {

	public static EntityMetadataConfigurationUnit configuration( EntityMetadataConfigurationUnitBuilder configurationBuilder ) {
		return configurationBuilder.nameExtractor( new LineItemNameExtractor() ).build();
	}

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
				.screenName( "Administration of Test Line Items" )
				.menuName( "Test Line Items Domain" ).build();
	}

}
