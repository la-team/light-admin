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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.core.reporting.Problem.ProblemLevel.ERROR;
import static org.lightadmin.core.reporting.Problem.ProblemLevel.WARNING;

public class DefaultProblemReporter implements ProblemReporter {

    public static final ProblemReporter INSTANCE = new DefaultProblemReporter();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DefaultProblemReporter() {
    }

    @Override
    public void handle(final Problem problem) {
        handle(newArrayList(problem));
    }

    @Override
    public void handle(Collection<? extends Problem> problems) {
        for (Problem warning : select(problems, WARNING)) {
            logger.warn(warning.getMessage());
        }

        Collection<? extends Problem> errors = select(problems, ERROR);
        if (!errors.isEmpty()) {
            throw new ConfigurationProblemException(errors);
        }
    }

    private Collection<? extends Problem> select(Collection<? extends Problem> problems, final Problem.ProblemLevel problemLevel) {
        return Collections2.filter(problems, new Predicate<Problem>() {
            @Override
            public boolean apply(Problem problem) {
                return problem.getProblemLevel() == problemLevel;
            }
        });
    }
}