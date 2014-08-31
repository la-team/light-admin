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
package org.lightadmin.reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

class ProblemReporterLogImpl implements ProblemReporter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void fatal(final Problem problem) {
        logger.error(problem.getMessage(), problem.getRootCause());
    }

    @Override
    public void error(final Problem problem) {
        logger.error(problem.getMessage(), problem.getRootCause());
    }

    @Override
    public void errors(Collection<? extends Problem> problems) {
        for (Problem problem : problems) {
            error(problem);
        }
    }

    @Override
    public void warning(final Problem problem) {
        logger.warn(problem.getMessage(), problem.getRootCause());
    }

    @SuppressWarnings("unused")
    public void setLogger(Logger logger) {
        this.logger = (logger != null ? logger : LoggerFactory.getLogger(getClass()));
    }
}