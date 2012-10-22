package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.reporting.Problem;

public class DslConfigurationProblem extends Problem {

	private final DslConfigurationClass configurationClass;

	public DslConfigurationProblem( final DslConfigurationClass configurationClass, final String message ) {
		super( message );
		this.configurationClass = configurationClass;
	}

	public DslConfigurationProblem( final DslConfigurationClass configurationClass, final String message, Throwable rootCause ) {
		super( message, rootCause );
		this.configurationClass = configurationClass;
	}

	@Override
	public String getMessage() {
		return String.format( "DSL Configuration %s: %s", configurationClass.getConfigurationClass().getSimpleName(), super.getMessage() );
	}

	@Override
	public String toString() {
		return getMessage();
	}
}