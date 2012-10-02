package org.lightadmin.core.config.support;

import com.google.common.base.Function;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.view.DefaultScreenContext;
import org.lightadmin.core.view.ScreenContext;
import org.lightadmin.core.view.support.Builder;
import org.lightadmin.core.view.support.filter.DefaultFilterBuilder;
import org.lightadmin.core.view.support.filter.FilterBuilder;
import org.lightadmin.core.view.support.filter.Filters;
import org.lightadmin.core.view.support.fragment.Fragment;
import org.lightadmin.core.view.support.fragment.FragmentBuilder;
import org.lightadmin.core.view.support.fragment.TableFragmentBuilder;
import org.lightadmin.core.view.support.scope.DefaultScopeBuilder;
import org.lightadmin.core.view.support.scope.ScopeBuilder;
import org.lightadmin.core.view.support.scope.Scopes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.ReflectionUtils.invokeMethod;

public class ConfigurationClassToBeanDefinitionTransformation implements Function<Class<?>, BeanDefinition> {

	@Override
	public BeanDefinition apply( final Class<?> dslConfiguration ) {
		Assert.notNull( dslConfiguration );

		final Administration administrationAnnotation = findAnnotation( dslConfiguration, Administration.class );

		Assert.notNull( administrationAnnotation );

		return domainTypeAdministrationConfigBeanDefinition( administrationAnnotation.value(), dslConfiguration );
	}

	private BeanDefinition domainTypeAdministrationConfigBeanDefinition( final Class<?> domainType, final Class<?> configurationClass ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( DomainTypeAdministrationConfiguration.class );

		builder.addConstructorArgValue( domainType );
		builder.addConstructorArgReference( repositoryBeanName( domainType ) );

		builder.addPropertyValue( "listViewFragment", listViewFragment( configurationClass ) );

		builder.addPropertyValue( "screenContext", screenContext( configurationClass ) );

		builder.addPropertyValue( "scopes", scopes( configurationClass ) );

		builder.addPropertyValue( "filters", filters( configurationClass ) );

		return builder.getBeanDefinition();
	}

	private Filters filters( final Class<?> configurationClass ) {
		return invokeMethodWithBuilder( configurationClass, "filters", FilterBuilder.class, DefaultFilterBuilder.class );
	}

	private Scopes scopes( final Class<?> configurationClass ) {
		return invokeMethodWithBuilder( configurationClass, "scopes", ScopeBuilder.class, DefaultScopeBuilder.class );
	}

	private Fragment listViewFragment( final Class<?> configurationClass ) {
		return invokeMethodWithBuilder( configurationClass, "listView", FragmentBuilder.class, TableFragmentBuilder.class );
	}

	private ScreenContext screenContext( final Class<?> configurationClass ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, "configureScreen", ScreenContext.class );

		ScreenContext screenContext = new DefaultScreenContext();
		if ( method != null ) {
			invokeMethod( method, null, screenContext );
		}

		return screenContext;
	}

	private <T> T invokeMethodWithBuilder( final Class<?> configurationClass, String methodName, Class<? extends Builder<T>> builderInterface, Class<? extends Builder<T>> concreteBuilderClass ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, methodName, builderInterface );

		final Builder<T> builder = BeanUtils.instantiateClass( concreteBuilderClass );
		if ( method != null ) {
			return ( T ) invokeMethod( method, null, builder );
		}

		return builder.build();
	}

	private String repositoryBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "Repository";
	}
}