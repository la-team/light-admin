package org.lightadmin.core.reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProblemReporterLogImpl implements ProblemReporter {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Override
	public void fatal( final Problem problem ) {
		logger.error( problem.getMessage(), problem.getRootCause() );
	}

	@Override
	public void error( final Problem problem ) {
		logger.error( problem.getMessage(), problem.getRootCause() );
	}

	@Override
	public void warning( final Problem problem ) {
		logger.warn( problem.getMessage(), problem.getRootCause() );
	}

	@SuppressWarnings( "unused" )
	public void setLogger( Logger logger ) {
		this.logger = ( logger != null ? logger : LoggerFactory.getLogger( getClass() ) );
	}
}