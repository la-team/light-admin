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
package org.lightadmin.core.storage;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;

public class OperationBuilder {

    private GlobalAdministrationConfiguration configuration;
    private LightAdminConfiguration lightAdminConfiguration;

    private OperationBuilder(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        this.configuration = configuration;
        this.lightAdminConfiguration = lightAdminConfiguration;
    }

    public static OperationBuilder operationBuilder(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        return new OperationBuilder(configuration, lightAdminConfiguration);
    }

    public SaveFileRestOperation saveOperation(Object entity) {
        return new SaveFileRestOperation(configuration, lightAdminConfiguration, entity);
    }

    public DeleteFileRestOperation deleteOperation(Object entity) {
        return new DeleteFileRestOperation(configuration, lightAdminConfiguration, entity);
    }

    public GetFileRestOperation getOperation(Object entity) {
        return new GetFileRestOperation(configuration, lightAdminConfiguration, entity);
    }

    public FileExistsRestOperation fileExistsOperation(Object entity) {
        return new FileExistsRestOperation(configuration, lightAdminConfiguration, entity);
    }
}