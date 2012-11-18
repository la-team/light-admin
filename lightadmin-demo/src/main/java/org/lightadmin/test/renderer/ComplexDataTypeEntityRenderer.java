package org.lightadmin.test.renderer;


import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.test.model.ComplexDataTypeEntity;
import org.lightadmin.test.model.ParentTestEntity;
import org.springframework.util.StringUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class ComplexDataTypeEntityRenderer implements FieldValueRenderer<ParentTestEntity> {

	@Override
	public String apply( final ParentTestEntity parentTestEntity ) {
		Set<String> complexDataTypeEntityDescriptions = newLinkedHashSet();

		for ( ComplexDataTypeEntity complexTypeEntity : parentTestEntity.getComplexTypeEntities() ) {
			complexDataTypeEntityDescriptions.add(
					"Complex Entity name: " + complexTypeEntity.getName() +
							"; Child Entity name: " + complexTypeEntity.getChildEntityName() );
		}

		return StringUtils.collectionToDelimitedString( complexDataTypeEntityDescriptions, "<br/>" );
	}
}
