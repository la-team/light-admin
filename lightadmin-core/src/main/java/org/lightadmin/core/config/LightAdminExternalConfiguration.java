package org.lightadmin.core.config;

import org.lightadmin.core.config.mbeans.LightAdminConfigurationMonitoringServiceMBean;
import org.lightadmin.core.config.rmi.GlobalConfigurationManagementRMIService;
import org.lightadmin.core.config.rmi.GlobalConfigurationManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;

@Configuration
public class LightAdminExternalConfiguration {

	@Bean
	public GlobalConfigurationManagementService globalConfigurationManagementService() {
		return new GlobalConfigurationManagementRMIService();
	}

	@Bean
	public RmiServiceExporter rmiServiceExporter() throws RemoteException {
		RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
		rmiServiceExporter.setServiceName( "GlobalConfigurationManagementService" );
		rmiServiceExporter.setService( globalConfigurationManagementService() );
		rmiServiceExporter.setServiceInterface( GlobalConfigurationManagementService.class );
		rmiServiceExporter.setRegistryPort( 1199 );
		rmiServiceExporter.afterPropertiesSet();
		return rmiServiceExporter;
	}

	@Bean
	public AnnotationMBeanExporter annotationMBeanExporter() {
		return new AnnotationMBeanExporter();
	}

	@Bean
	public LightAdminConfigurationMonitoringServiceMBean lightAdminConfigurationMonitoringServiceMBean() {
		return new LightAdminConfigurationMonitoringServiceMBean();
	}

}