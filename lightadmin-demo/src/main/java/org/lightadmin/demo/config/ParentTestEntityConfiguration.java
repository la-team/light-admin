package org.lightadmin.demo.config;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.demo.model.ComplexDataTypeEntity;
import org.lightadmin.demo.model.ParentTestEntity;
import org.springframework.util.StringUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

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
				.field( "complexTypeEntities" ).alias( "Complex Type Entities" ).renderer( complexDataTypeEntityRenderer() ).build();
	}

	private static FieldValueRenderer<ParentTestEntity> complexDataTypeEntityRenderer() {
		return new FieldValueRenderer<ParentTestEntity>() {

			@Override
			public String apply( final ParentTestEntity parentTestEntity ) {
				Set<String> complexDataTypeEntityDescriptions = newLinkedHashSet();

				for ( ComplexDataTypeEntity complexTypeEntity : parentTestEntity.getComplexTypeEntities() ) {
					complexDataTypeEntityDescriptions.add(
							"Entity name: " + complexTypeEntity.getName() +
							" child entity name: " + complexTypeEntity.getChildEntityName() );
				}

				return StringUtils.collectionToDelimitedString( complexDataTypeEntityDescriptions, "\n" );
			}
		};
	}
}
