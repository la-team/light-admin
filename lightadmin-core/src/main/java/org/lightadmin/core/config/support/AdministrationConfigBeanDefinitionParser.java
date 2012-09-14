package org.lightadmin.core.config.support;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.core.repository.support.DynamicJpaRepositoryFactoryBean;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.view.preparer.*;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class AdministrationConfigBeanDefinitionParser implements BeanDefinitionParser {

	private static final String BASE_PACKAGE = "base-package";

	@Override
	public BeanDefinition parse( final Element element, final ParserContext parserContext ) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false );
		provider.addIncludeFilter( new AnnotationTypeFilter( Administration.class ) );

		final String basePackage = element.getAttribute( BASE_PACKAGE );

		final Map<Class<?>, RuntimeBeanReference> domainTypeConfigurations = new ManagedMap<Class<?>, RuntimeBeanReference>(  );

		for ( BeanDefinition definition : provider.findCandidateComponents( basePackage ) ) {
			final AnnotatedBeanDefinition annotatedBeanDefinition = ( AnnotatedBeanDefinition ) definition;

			final Class<?> configurationClass = configurationClass( annotatedBeanDefinition );
			final Class<?> domainType = domainType( annotatedBeanDefinition );

			registerDomainRepository( domainType, parserContext );

			String domainConfigurationBeanName = registerDomainTypeAdministrationConfiguration( parserContext, domainType, configurationClass );

			domainTypeConfigurations.put( domainType, new RuntimeBeanReference( domainConfigurationBeanName ) );
		}

		registerGlobalAdministrationConfiguration( parserContext, domainTypeConfigurations );

		registerRepositoryExporter( parserContext );

		registerViewPreparers( parserContext );

		return null;
	}

	private void registerViewPreparers( final ParserContext parserContext ) {
		registerViewPreparer( "footerSectionViewPreparer", FooterSectionViewPreparer.class, parserContext );
		registerViewPreparer( "headerSectionViewPreparer", HeaderSectionViewPreparer.class, parserContext );
		registerViewPreparer( "leftSectionViewPreparer", LeftSectionViewPreparer.class, parserContext );
		registerViewPreparer( "listViewPreparer", ListViewPreparer.class, parserContext );
		registerViewPreparer( "screenViewPreparer", ScreenViewPreparer.class, parserContext );
		registerViewPreparer( "dashboardViewPreparer", DashboardViewPreparer.class, parserContext );
	}

	private void registerViewPreparer( final String viewPreparerName, final Class<? extends ViewContextPreparer> viewPreparerClass, final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( viewPreparerClass );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, viewPreparerName ) );
	}

	private Set<Pair<String, String>> listColumns( final Class<?> configurationClass ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, "listColumns" );

		Set<Pair<String, String>> listColumns = newLinkedHashSet();
		if ( method != null ) {
			listColumns.addAll( ( Set<Pair<String, String>> ) ReflectionUtils.invokeMethod( method, null ) );
		}
		return listColumns;
	}

	private Class<?> configurationClass( final AnnotatedBeanDefinition definition ) {
		final String configurationClassName = definition.getMetadata().getClassName();
		return ClassUtils.resolveClassName( configurationClassName, ClassUtils.getDefaultClassLoader() );
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
		builder.addPropertyValue( "listColumns", listColumns( configurationClass ) );
		return builder.getBeanDefinition();
	}

	private void registerGlobalAdministrationConfiguration( final ParserContext parserContext, final Map<Class<?>, RuntimeBeanReference> domainTypeConfigurations ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( GlobalAdministrationConfiguration.class );
		builder.addPropertyValue( "domainTypeConfigurations", domainTypeConfigurations );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, "globalAdministrationConfiguration" ) );
	}

	private void registerRepositoryExporter( final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( DynamicJpaRepositoryExporter.class );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, "jpaRepositoryExporter" ) );
	}

	private String registerDomainRepository( final Class<?> domainType, final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( DynamicJpaRepositoryFactoryBean.class );
		builder.addConstructorArgValue( domainType );
		builder.addPropertyValue( "repositoryInterface", DynamicJpaRepository.class );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		final String repositoryBeanName = repositoryBeanName( domainType );
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, repositoryBeanName ) );
		return repositoryBeanName;
	}

	private String repositoryBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "Repository";
	}

	private String domainTypeConfigurationBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "AdministrationConfiguration";
	}

	private Class<?> domainType( final AnnotatedBeanDefinition definition ) {
		return AnnotationAttributes.fromMap( definition.getMetadata().getAnnotationAttributes( Administration.class.getName(), false ) ).getClass( "value" );
	}
}