package org.lightadmin.core.config.support;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.core.repository.support.DynamicJpaRepositoryFactoryBean;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.view.DefaultScreenContext;
import org.lightadmin.core.view.ScreenContext;
import org.lightadmin.core.view.preparer.*;
import org.lightadmin.core.view.support.Fragment;
import org.lightadmin.core.view.support.FragmentBuilder;
import org.lightadmin.core.view.support.TableFragmentBuilder;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.util.Map;

public class AdministrationConfigBeanDefinitionParser implements BeanDefinitionParser {

	private static final String SPRING_SECURITY_CONTEXT_RESOURCE = "classpath*:META-INF/spring/spring-security.xml";

	private static final String BASE_PACKAGE = "base-package";

	@Override
	public BeanDefinition parse( final Element element, final ParserContext parserContext ) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false );
		provider.addIncludeFilter( new AnnotationTypeFilter( Administration.class ) );

		final String basePackage = element.getAttribute( BASE_PACKAGE );

		final Map<Class<?>, RuntimeBeanReference> domainTypeConfigurations = new ManagedMap<Class<?>, RuntimeBeanReference>();

		loadSpringSecurityDefinitions( parserContext );

		for ( BeanDefinition definition : provider.findCandidateComponents( basePackage ) ) {
			final AnnotatedBeanDefinition annotatedBeanDefinition = ( AnnotatedBeanDefinition ) definition;

			final Class<?> configurationClass = configurationClass( annotatedBeanDefinition );

			final Class<?> domainType = configurationClass.getAnnotation( Administration.class ).value();

			registerDomainRepository( domainType, parserContext );

			String domainConfigurationBeanName = registerDomainTypeAdministrationConfiguration( parserContext, domainType, configurationClass );

			domainTypeConfigurations.put( domainType, new RuntimeBeanReference( domainConfigurationBeanName ) );
		}

		registerGlobalAdministrationConfiguration( parserContext, domainTypeConfigurations );

		registerRepositoryRestConfiguration( parserContext );

		registerRepositoryExporter( parserContext );

		registerViewPreparers( parserContext );

		return null;
	}

	private void loadSpringSecurityDefinitions( final ParserContext parserContext ) {
		BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader( parserContext.getRegistry() );
		beanDefinitionReader.loadBeanDefinitions( SPRING_SECURITY_CONTEXT_RESOURCE );
	}

	private void registerRepositoryRestConfiguration( final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( RepositoryRestConfiguration.class );
		builder.addPropertyValue( "defaultPageSize", 5 );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, "repositoryRestConfiguration" ) );
	}

	private void registerViewPreparers( final ParserContext parserContext ) {
		registerSimpleBean( "footerSectionViewPreparer", FooterSectionViewPreparer.class, parserContext );
		registerSimpleBean( "headerSectionViewPreparer", HeaderSectionViewPreparer.class, parserContext );
		registerSimpleBean( "leftSectionViewPreparer", LeftSectionViewPreparer.class, parserContext );
		registerSimpleBean( "listViewPreparer", ListViewPreparer.class, parserContext );
		registerSimpleBean( "screenViewPreparer", ScreenViewPreparer.class, parserContext );
		registerSimpleBean( "dashboardViewPreparer", DashboardViewPreparer.class, parserContext );
	}

	private String registerDomainTypeAdministrationConfiguration( final ParserContext parserContext, Class<?> domainType, final Class<?> configurationClass ) {
		AbstractBeanDefinition beanDefinition = domainTypeAdministrationConfigBeanDefinition( domainType, configurationClass );
		final String beanName = domainTypeConfigurationBeanName( domainType );
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, beanName ) );
		return beanName;
	}

	private AbstractBeanDefinition domainTypeAdministrationConfigBeanDefinition( final Class<?> domainType, final Class<?> configurationClass ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( DomainTypeAdministrationConfiguration.class );
		builder.addConstructorArgValue( domainType );
		builder.addConstructorArgReference( repositoryBeanName( domainType ) );
		builder.addPropertyValue( "listViewFragment", listViewFragment( configurationClass ) );
		builder.addPropertyValue( "screenContext", screenContext( configurationClass ) );
		return builder.getBeanDefinition();
	}

	private void registerGlobalAdministrationConfiguration( final ParserContext parserContext, final Map<Class<?>, RuntimeBeanReference> domainTypeConfigurations ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( GlobalAdministrationConfiguration.class );
		builder.addPropertyValue( "domainTypeConfigurations", domainTypeConfigurations );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, "globalAdministrationConfiguration" ) );
	}

	private void registerRepositoryExporter( final ParserContext parserContext ) {
		registerSimpleBean( "jpaRepositoryExporter", DynamicJpaRepositoryExporter.class, parserContext );
	}

	private String registerDomainRepository( final Class<?> domainType, final ParserContext parserContext ) {
		final String repositoryBeanName = repositoryBeanName( domainType );

		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( DynamicJpaRepositoryFactoryBean.class );
		builder.addConstructorArgValue( domainType );
		builder.addPropertyValue( "repositoryInterface", DynamicJpaRepository.class );

		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, repositoryBeanName ) );

		return repositoryBeanName;
	}

	private void registerSimpleBean( final String viewPreparerName, final Class<?> viewPreparerClass, final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( viewPreparerClass );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, viewPreparerName ) );
	}

	private Class<?> configurationClass( final AnnotatedBeanDefinition definition ) {
		final String configurationClassName = definition.getMetadata().getClassName();
		return ClassUtils.resolveClassName( configurationClassName, ClassUtils.getDefaultClassLoader() );
	}

	private Fragment listViewFragment( final Class<?> configurationClass ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, "listView", FragmentBuilder.class );

		FragmentBuilder fragmentBuilder = new TableFragmentBuilder();
		if ( method != null ) {
			return ( Fragment ) ReflectionUtils.invokeMethod( method, null, fragmentBuilder );
		}

		return fragmentBuilder.build();
	}

	private ScreenContext screenContext( final Class<?> configurationClass ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, "configureScreen", ScreenContext.class );

		ScreenContext screenContext = new DefaultScreenContext();
		if ( method != null ) {
			ReflectionUtils.invokeMethod( method, null, screenContext );
		}

		return screenContext;
	}

	private String repositoryBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "Repository";
	}

	private String domainTypeConfigurationBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "AdministrationConfiguration";
	}
}