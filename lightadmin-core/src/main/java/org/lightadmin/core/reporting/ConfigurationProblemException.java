package org.lightadmin.core.reporting;

import java.util.Collection;

public class ConfigurationProblemException extends RuntimeException {

    public ConfigurationProblemException(Problem problem) {
        super(problem.getMessage());
    }

    public ConfigurationProblemException(Collection<? extends Problem> problems) {
        super(combineMessages(problems));
    }

    private static String combineMessages(Collection<? extends Problem> problems) {
        final StringBuilder messageBuilder = new StringBuilder();
        for (Problem problem : problems) {
            messageBuilder.append(problem.getMessage()).append('\n');
        }
        return messageBuilder.toString();
    }
}