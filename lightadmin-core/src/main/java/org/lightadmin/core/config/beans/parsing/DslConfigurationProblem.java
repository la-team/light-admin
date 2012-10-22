package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.reporting.Problem;

public class DslConfigurationProblem extends Problem {

	private final DslConfigurationClass configurationClass;

	private DslConfigurationUnit configurationUnit;

	public DslConfigurationProblem( final DslConfigurationClass configurationClass, final String message ) {
		super( message );
		this.configurationClass = configurationClass;
	}

	public DslConfigurationProblem( final DslConfigurationClass configurationClass, DslConfigurationUnit configurationUnit, final String message ) {
		this( configurationClass, message );
		this.configurationUnit = configurationUnit;
	}

	@Override
	public String getMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( "DSL Configuration " );
		stringBuilder.append( "\"" ).append( configurationClass.getConfigurationClass().getSimpleName() ).append( "\"" ).append( ": " );
		if ( configurationUnit != null ) {
			stringBuilder.append( "Unit " ).append( "\"" ).append( configurationUnit.getName() ).append( "\"" ).append( ": " );
		}
		stringBuilder.append( super.getMessage() );

		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		return getMessage();
	}
}