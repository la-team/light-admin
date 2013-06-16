package org.lightadmin.core.config.domain.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import static java.util.Arrays.asList;

import javax.servlet.jsp.tagext.SimpleTag;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.view.editor.EnumFieldEditControl;

import com.google.common.base.Function;
import static com.google.common.collect.Lists.transform;

public class PersistentFieldSetConfigurationUnitBuilderAdapter implements PersistentFieldSetConfigurationUnitBuilder {

	private final GenericFieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder;

	public PersistentFieldSetConfigurationUnitBuilderAdapter( Class<?> domainType, DomainConfigurationUnitType configurationUnitType ) {
		this.fieldSetConfigurationUnitBuilder = new GenericFieldSetConfigurationUnitBuilder( domainType, configurationUnitType );
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder field( final String fieldName ) {
		fieldSetConfigurationUnitBuilder.field( fieldName );

		return this;
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder caption( final String caption ) {
		fieldSetConfigurationUnitBuilder.caption( caption );

		return this;
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder enumeration( String... values ) {
		List<EnumElement> elements = new ArrayList<EnumElement>( values.length );
		for ( String value : values ) {
			elements.add( EnumElement.element( value, value ) );
		}
		return enumeration( elements );
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder enumeration( EnumElement... elements ) {
		return enumeration( asList( elements ) );
	}


	private PersistentFieldSetConfigurationUnitBuilder enumeration( List<EnumElement> elements ) {
		return withCustomEditor(new EnumFieldEditControl( elements ) );
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder withCustomEditor( SimpleTag editControl ) {
		fieldSetConfigurationUnitBuilder.withCustomControl( editControl );

		return this;
	}

	@Override
	public FieldSetConfigurationUnit build() {
		return fieldSetConfigurationUnitBuilder.build();
	}
}
