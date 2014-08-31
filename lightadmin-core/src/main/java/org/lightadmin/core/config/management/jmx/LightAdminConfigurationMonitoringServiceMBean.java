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
package org.lightadmin.core.config.management.jmx;

import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@ManagedResource(objectName = LightAdminConfigurationMonitoringServiceMBean.MBEAN_NAME,
        description = "LightAdmin DomainType Administration Configuration Management Service")
public class LightAdminConfigurationMonitoringServiceMBean {

    public static final String MBEAN_NAME = "org.lightadmin.mbeans:type=config,name=LightAdminConfigurationMonitoringServiceMBean";

    private final GlobalConfigurationManagementService globalConfigurationManagementService;

    public LightAdminConfigurationMonitoringServiceMBean(final GlobalConfigurationManagementService globalConfigurationManagementService) {
        this.globalConfigurationManagementService = globalConfigurationManagementService;
    }

    @ManagedOperation(description = "List all registered Domain Type Configurations")
    public Set<String> getDomainTypeAdministrationConfigurations() {
        final Set<String> result = newLinkedHashSet();
        for (DomainTypeAdministrationConfiguration configuration : globalConfigurationManagementService.getRegisteredDomainTypeConfigurations()) {
            result.add(configuration.getConfigurationName());
        }
        return result;
    }

    @ManagedOperation(description = "List all registered Domain Types")
    public Set<String> getDomainTypes() {
        final Set<String> result = newLinkedHashSet();
        for (DomainTypeAdministrationConfiguration configuration : globalConfigurationManagementService.getRegisteredDomainTypeConfigurations()) {
            result.add(configuration.getDomainTypeName());
        }
        return result;
    }
}