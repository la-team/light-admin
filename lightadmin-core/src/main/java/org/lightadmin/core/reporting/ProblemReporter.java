package org.lightadmin.core.reporting;

public interface ProblemReporter {

	void fatal( Problem problem );

	void error( Problem problem );

	void warning( Problem problem );
}