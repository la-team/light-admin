package org.lightadmin.core.config.context;

import org.lightadmin.core.config.management.jmx.LightAdminConfigurationMonitoringServiceMBean;
import org.lightadmin.core.config.management.rmi.DataManipulationService;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
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