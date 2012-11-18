package org.lightadmin.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;

import org.lightadmin.test.model.ParentTestEntity;
import org.lightadmin.test.renderer.ComplexDataTypeEntityRenderer;

@SuppressWarnings( "unused" )
@Administration( ParentTestEntity.class )
public class ParentTestEntityConfiguration {

	public static ScreenContextConfigurationUnit screenContext( ScreenContextConfigurationUnitBuilder screenContextBuilder ) {
		return screenContextBuilder
				.screenName( "Administration of Domain with Complex Data Type" )
				.menuName( "Domain with Complex Data Type" ).build();
	}

	public static ListViewConfigurationUnit listView( ListViewConfigurationUnitBuilder listViewBuilder ) {
		return listViewBuilder
				.field( "id" ).alias( "ID" )
				.field( "name" ).alias( "Name" )
				.field( "complexTypeEntities" )
				.alias( "Complex Type Entities" ).renderer( new ComplexDataTypeEntityRenderer() ).build();
	}
}
