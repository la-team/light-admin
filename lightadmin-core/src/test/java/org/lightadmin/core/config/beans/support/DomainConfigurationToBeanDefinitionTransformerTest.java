package org.lightadmin.core.config.beans.support;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.util.ClassUtils;

import static org.junit.Assert.*;
import static org.lightadmin.core.test.util.BeanDefinitionUtils.constructorArgValue;
import static org.lightadmin.core.test.util.BeanDefinitionUtils.propertyValue;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.*;

public class DomainConfigurationToBeanDefinitionTransformerTest {

	private DomainConfigurationToBeanDefinitionTransformer subject;

	@Before
	public void setup() {
		subject = new DomainConfigurationToBeanDefinitionTransformer();
	}

	@Test( expected = IllegalArgumentException.class )
	public void nullConfigurationNotAllowed() {
		subject.apply( null );
	}

	@Test
	public void emptyConfigurationBeanDefinitionCreation() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertNotNull( beanDefinition );
	}

	@Test
	public void beanDefinitionHasCorrectBeanClass() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertEquals( DomainTypeAdministrationConfiguration.class.getName(), beanDefinition.getBeanClassName() );
	}

	@Test
	public void beanDefinitionInitializedWithDomainType() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertEquals( DomainEntity.class, constructorArgValue( beanDefinition, 0 ) );
	}

	@Test
	public void beanDefinitionInitializedWithRepositoryBeanReference() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertEquals( RuntimeBeanReference.class, constructorArgValue( beanDefinition, 1 ).getClass() );
	}

	@Test
	public void emptyEntityConfigurationCreatedIfNotDefined() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertDefaultConfigurationUnitCreated( beanDefinition, "entityConfiguration", EntityConfiguration.class );
	}

	@Test
	public void emptyScreenContextCreatedIfNotDefined() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertDefaultConfigurationUnitCreated( beanDefinition, "screenContext", ScreenContext.class );
	}

	@Test
	public void emptyListViewFragmentCreatedIfNotDefined() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertDefaultConfigurationUnitCreated( beanDefinition, "listViewFragment", Fragment.class );
	}

	@Test
	public void emptyScopesCreatedIfNotDefined() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertDefaultConfigurationUnitCreated( beanDefinition, "scopes", Scopes.class );
	}

	@Test
	public void emptyFiltersCreatedIfNotDefined() {
		final BeanDefinition beanDefinition = subject.apply( emptyDomainEntityConfiguration() );

		assertDefaultConfigurationUnitCreated( beanDefinition, "filters", Filters.class );
	}

	@Test
	public void emptyConfigurationUnitCreatedInCaseOfException() {
		final BeanDefinition beanDefinition = subject.apply( domainEntityConfigurationWithException() );

		assertDefaultConfigurationUnitCreated( beanDefinition, "entityConfiguration", EntityConfiguration.class );
	}

	private static void assertDefaultConfigurationUnitCreated( BeanDefinition beanDefinition, String configurationPropertyName, Class configurationUnitClass ) {
		final Object configurationUnit = propertyValue( beanDefinition, configurationPropertyName );

		assertNotNull( configurationUnit );
		assertTrue( ClassUtils.isAssignableValue( configurationUnitClass, configurationUnit ) );
	}
}