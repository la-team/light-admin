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
package org.lightadmin.core.config.management.rmi;

import org.lightadmin.api.config.management.rmi.DataManipulationService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.init.DatabasePopulatorUtils.execute;

public class DataManipulationServiceImpl implements DataManipulationService {

    private DataSource dataSource;

    public DataManipulationServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void truncateDatabase() {
        execute(databasePopulator("classpath:db/truncate.sql"), this.dataSource);
    }

    @Override
    public void populateDatabase() {
        execute(databasePopulator("classpath:db/data.sql"), this.dataSource);
    }

    private ResourceDatabasePopulator databasePopulator(final String location) {
        final ResourceLoader resourceLoader = new DefaultResourceLoader();
        final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(resourceLoader.getResource(location));
        return databasePopulator;
    }
}