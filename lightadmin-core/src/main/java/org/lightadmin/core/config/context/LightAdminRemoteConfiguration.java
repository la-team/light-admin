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
package org.lightadmin.core.config.context;

import org.lightadmin.api.config.management.rmi.DataManipulationService;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.management.jmx.LightAdminConfigurationMonitoringServiceMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.util.ClassUtils;

import java.rmi.RemoteException;

@Profile("test-env")
@Configuration
public class LightAdminRemoteConfiguration {

    @Autowired
    private GlobalConfigurationManagementService globalConfigurationManagementService;

    @Autowired
    private DataManipulationService dataManipulationService;

    @Bean
    public RmiServiceExporter globalConfigurationManagementServiceRmiExporter() throws RemoteException {
        return createRmiServiceExporter(globalConfigurationManagementService, "GlobalConfigurationManagementService");
    }

    @Bean
    public RmiServiceExporter dataManipulationServiceRmiExporter() throws RemoteException {
        return createRmiServiceExporter(dataManipulationService, "DataManipulationService");
    }

    @Bean
    public AnnotationMBeanExporter annotationMBeanExporter() {
        return new AnnotationMBeanExporter();
    }

    @Bean
    public LightAdminConfigurationMonitoringServiceMBean lightAdminConfigurationMonitoringServiceMBean() {
        return new LightAdminConfigurationMonitoringServiceMBean(globalConfigurationManagementService);
    }

    private RmiServiceExporter createRmiServiceExporter(final Object service, final String serviceName) throws RemoteException {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName(serviceName);
        rmiServiceExporter.setService(service);
        rmiServiceExporter.setServiceInterface(ClassUtils.getAllInterfaces(service)[0]);
        rmiServiceExporter.setRegistryPort(1199);
        rmiServiceExporter.afterPropertiesSet();
        return rmiServiceExporter;
    }
}