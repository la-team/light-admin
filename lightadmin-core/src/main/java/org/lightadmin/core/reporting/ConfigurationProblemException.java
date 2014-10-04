/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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