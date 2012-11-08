package org.lightadmin.core.config;

import org.lightadmin.core.config.management.jmx.LightAdminConfigurationMonitoringServiceMBean;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementRMIService;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.rest.HttpMessageConverterRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;

@Configuration
public class LightAdminRemoteConfiguration {

	@Bean
	public GlobalConfigurationManagementService globalConfigurationManagementService() {
		return new GlobalConfigurationManagementRMIService();
	}

	@Bean
	public HttpMessageConverterRefresher httpMessageConverterRefresher() {
		return new HttpMessageConverterRefresher();
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