package org.lightadmin.core.reporting;

import java.util.Collection;

class FailFastProblemReporter implements ProblemReporter {

    @Override
    public void fatal(final Problem problem) {
        throw new ConfigurationProblemException(problem);
    }

    @Override
    public void error(final Problem problem) {
        throw new ConfigurationProblemException(problem);
    }

    @Override
    public void errors(Collection<? extends Problem> problems) {
        throw new ConfigurationProblemException(problems);
    }

    @Override
    public void warning(final Problem problem) {
        throw new ConfigurationProblemException(problem);
    }
}