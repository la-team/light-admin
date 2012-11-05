package org.lightadmin.core.reporting;

public class ConfigurationProblemException extends RuntimeException {

	public ConfigurationProblemException( Problem problem ) {
		super( problem.getMessage() );
	}
}