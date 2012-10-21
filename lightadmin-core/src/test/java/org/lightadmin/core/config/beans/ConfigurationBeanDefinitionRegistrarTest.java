package org.lightadmin.core.config.beans;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.lightadmin.core.test.util.BeanDefinitionUtils.propertyValue;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.DomainEntity;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.DomainEntityEmptyConfiguration;

public class ConfigurationBeanDefinitionRegistrarTest {

	private static final String GLOBAL_CONFIGURATION_BEAN_NAME = "org.lightadmin.core.config.globalAdministrationConfiguration";
	private static final String DOMAIN_ENTIY_CONFIGURATION_BEAN_NAME = "org.lightadmin.core.config.domainEntityAdministrationConfiguration";

	private ConfigurationBeanDefinitionRegistrar subject;

	@Test
	public void registerGlobalConfigurationWithoutDomains() throws Exception {
		subject = new ConfigurationBeanDefinitionRegistrar();

		final Capture<BeanDefinition> beanDefinitionCapture = new Capture<BeanDefinition>();

		final BeanDefinitionRegistry beanDefinitionRegistry = EasyMock.createMock( BeanDefinitionRegistry.class );

		beanDefinitionRegistry.registerBeanDefinition( EasyMock.eq( GLOBAL_CONFIGURATION_BEAN_NAME ), EasyMock.<BeanDefinition>capture( beanDefinitionCapture ) );
		EasyMock.expectLastCall().once();

		EasyMock.replay( beanDefinitionRegistry );

		subject.registerBeanDefinitions( beanDefinitionRegistry );

		EasyMock.verify( beanDefinitionRegistry );

		final BeanDefinition beanDefinition = beanDefinitionCapture.getValue();

		assertCorrectGlobalConfigurationBeanDefinitionRegistered( beanDefinition );

		assertTrue( ((ManagedMap) propertyValue( beanDefinition, "domainTypeConfigurations" )).isEmpty() );
	}

	@Test
	public void registerGlobalConfWithDomainTypeConfiguration() throws Exception {
		subject = new ConfigurationBeanDefinitionRegistrar( DomainEntityEmptyConfiguration.class );

		final Capture<BeanDefinition> globalConfigurationBeanDefinitionCapturer = new Capture<BeanDefinition>();
		final Capture<BeanDefinition> domainConfigurationBeanDefinitionCapturer = new Capture<BeanDefinition>();

		final BeanDefinitionRegistry beanDefinitionRegistry = EasyMock.createMock( BeanDefinitionRegistry.class );

		beanDefinitionRegistry.registerBeanDefinition( EasyMock.eq( GLOBAL_CONFIGURATION_BEAN_NAME ), EasyMock.<BeanDefinition>capture( globalConfigurationBeanDefinitionCapturer ) );
		EasyMock.expectLastCall().once();

		beanDefinitionRegistry.registerBeanDefinition( EasyMock.eq( DOMAIN_ENTIY_CONFIGURATION_BEAN_NAME ), EasyMock.<BeanDefinition>capture( domainConfigurationBeanDefinitionCapturer ) );
		EasyMock.expectLastCall().once();

		EasyMock.replay( beanDefinitionRegistry );

		subject.registerBeanDefinitions( beanDefinitionRegistry );

		EasyMock.verify( beanDefinitionRegistry );

		final BeanDefinition globalConfigBeanDefinition = globalConfigurationBeanDefinitionCapturer.getValue();
		final BeanDefinition domainConfigBeanDefinition = domainConfigurationBeanDefinitionCapturer.getValue();

		assertEquals( DomainTypeAdministrationConfiguration.class.getName(), domainConfigBeanDefinition.getBeanClassName() );

		final Map<Class, BeanReference> domainTypeConfigurations = registeredDomainTypeConfigurations( globalConfigBeanDefinition );

		assertEquals( 1, domainTypeConfigurations.size() );

		final Map.Entry<Class, BeanReference> classBeanReferenceEntry = domainTypeConfigurations.entrySet().iterator().next();

		assertEquals( DomainEntity.class, classBeanReferenceEntry.getKey() );
		assertEquals( DOMAIN_ENTIY_CONFIGURATION_BEAN_NAME, classBeanReferenceEntry.getValue().getBeanName() );
	}

	@SuppressWarnings( "unchecked" )
	private Map<Class, BeanReference> registeredDomainTypeConfigurations( final BeanDefinition globalConfigBeanDefinition ) {
		return ( Map<Class, BeanReference> ) propertyValue( globalConfigBeanDefinition, "domainTypeConfigurations" );
	}

	private static void assertCorrectGlobalConfigurationBeanDefinitionRegistered( final BeanDefinition beanDefinition ) {
		assertEquals( GlobalAdministrationConfiguration.class.getName(), beanDefinition.getBeanClassName() );

		assertEquals( ManagedMap.class, propertyValue( beanDefinition, "domainTypeConfigurations" ).getClass() );
	}
}