package org.lightadmin.core.reporting;

class FailFastProblemReporter implements ProblemReporter {

	@Override
	public void fatal( final Problem problem ) {
		throw new ConfigurationProblemException( problem );
	}

	@Override
	public void error( final Problem problem ) {
		throw new ConfigurationProblemException( problem );
	}

	@Override
	public void warning( final Problem problem ) {
		throw new ConfigurationProblemException( problem );
	}
}