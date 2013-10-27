package org.lightadmin.core.reporting;

import java.util.Collection;

public interface ProblemReporter {

    void fatal(Problem problem);

    void error(Problem problem);

    void errors(Collection<? extends Problem> problems);

    void warning(Problem problem);
}