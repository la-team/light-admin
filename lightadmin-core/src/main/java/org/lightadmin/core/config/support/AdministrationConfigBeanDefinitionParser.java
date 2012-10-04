package org.lightadmin.core.config.support;

import com.google.common.collect.Collections2;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfigurationPostProcessor;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.support.DynamicJpaRepositoryFactoryBean;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.view.preparer.*;
import org.lightadmin.core.view.renderer.BasicAttributeRendererFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.repository.context.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.springframework.util.StringUtils.uncapitalize;

public class AdministrationConfigBeanDefinitionParser implements BeanDefinitionParser {

	private static final String SPRING_SECURITY_CONTEXT_RESOURCE = "classpath*:META-INF/spring/spring-security.xml";

	private static final String FOOTER_SECTION_VIEW_PREPARER_BEAN_NAME = "footerSectionViewPreparer";
	private static final String HEADER_SECTION_VIEW_PREPARER_BEAN_NAME = "headerSectionViewPreparer";
	private static final String LEFT_SECTION_VIEW_PREPARER_BEAN_NAME = "leftSectionViewPreparer";
	private static final String LIST_VIEW_PREPARER_BEAN_NAME = "listViewPreparer";
	private static final String EDIT_VIEW_PREPARER_BEAN_NAME = "editViewPreparer";
	private static final String SHOW_VIEW_PREPARER_BEAN_NAME = "showViewPreparer";
	private static final String SCREEN_VIEW_PREPARER_BEAN_NAME = "screenViewPreparer";
	private static final String DASHBOARD_VIEW_PREPARER_BEAN_NAME = "dashboardViewPreparer";

	private static final String GLOBAL_ADMINISTRATION_CONFIGURATION_BEAN_NAME = "globalAdministrationConfiguration";

	private static final String JPA_REPOSITORY_EXPORTER_BEAN_NAME = "jpaRepositoryExporter";

	private static final String REPOSITORY_BEAN_NAME_FORMAT = "%sRepository";

	private static final String DOMAIN_CONFIGURATION_BEAN_NAME_FORMAT = "%sAdministrationConfiguration";

	private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
	private static final String ENTITY_MANAGER_FACTORY_REF_ATTRIBUTE = "entity-manager-factory-ref";

	@Override
	public BeanDefinition parse( final Element element, final ParserContext parserContext ) {
		loadSpringSecurityContext( parserContext );

		// TODO: max: For the future!
		BeanReference entityManagerFactoryRef = entityManagerFactoryRef( element );

		final Set<BeanDefinition> administrationConfigurationsDefinitions = findAdministrationConfigurations( scanBasePackage( element ) );

		final Set<Pair<Class, Class>> dslConfigurations = transformToDslConfigurations( administrationConfigurationsDefinitions );

		registerDomainTypeRepositories( domainTypes( dslConfigurations ), parserContext );

		registerConfigurationBeans( dslConfigurations, parserContext );

		registerBeanPostProcessors( parserContext );

		registerValidatingRepositoryEventListener( parserContext );

		registerRepositoryExporter( parserContext );

		registerRepositoryRestConfiguration( parserContext );

		registerViewPreparers( parserContext );

		registerRendererFactory( parserContext );

		return null;
	}

	private void registerBeanPostProcessors( final ParserContext parserContext ) {
		registerGlobalConfigurationPostProcessor( parserContext );
	}

	private void registerRendererFactory( final ParserContext parserContext ) {
		registerSimpleBean( BasicAttributeRendererFactory.class, parserContext );
	}

	private void registerGlobalConfigurationPostProcessor( final ParserContext parserContext ) {
		registerSimpleBean( GlobalAdministrationConfigurationPostProcessor.class, parserContext );
	}

	private BeanReference entityManagerFactoryRef( final Element element ) {
		if ( element.hasAttribute( ENTITY_MANAGER_FACTORY_REF_ATTRIBUTE ) ) {
			return new RuntimeBeanReference( element.getAttribute( ENTITY_MANAGER_FACTORY_REF_ATTRIBUTE ) );
		}
		return null;
	}

	private void registerConfigurationBeans( final Set<Pair<Class, Class>> dslConfigurations, final ParserContext parserContext ) {
		final Map<Class<?>, BeanReference> domainTypeConfigurations = registerDomainConfigurations( dslConfigurations, parserContext );

		registerGlobalAdministrationConfiguration( parserContext, domainTypeConfigurations );
	}

	private Map<Class<?>, BeanReference> registerDomainConfigurations( final Set<Pair<Class, Class>> dslConfigurations, final ParserContext parserContext ) {
		final Map<Class<?>, BeanReference> domainTypeConfigurations = new ManagedMap<Class<?>, BeanReference>();
		for ( Pair<Class, Class> configuration : dslConfigurations ) {
			final Class<?> domainType = configuration.first;
			final Class<?> configurationClass = configuration.second;

			final String beanName = domainTypeConfigurationBeanName( domainType );

			final BeanReference beanReference = registerDomainConfigurationBean( beanName, configurationClass, parserContext );

			domainTypeConfigurations.put( domainType, beanReference );
		}
		return domainTypeConfigurations;
	}

	private BeanReference registerDomainConfigurationBean( final String beanName, final Class<?> configurationClass, final ParserContext parserContext ) {
		final BeanDefinition domainConfigurationBeanDefinition = new ConfigurationClassToBeanDefinitionTransformation().apply( configurationClass );
		parserContext.registerBeanComponent( new BeanComponentDefinition( domainConfigurationBeanDefinition, beanName ) );
		return new RuntimeBeanReference( beanName );
	}

	private Collection<Class> domainTypes( final Set<Pair<Class, Class>> dynamicConfigurations ) {
		return Collections2.transform( dynamicConfigurations, Pair.<Class>extractFirstTransformer() );
	}

	private void registerDomainTypeRepositories( final Collection<Class> domainTypes, final ParserContext parserContext ) {
		for ( Class domainType : domainTypes ) {
			registerDomainRepository( domainType, parserContext );
		}
	}

	private Class<?> configurationDomainType( final Class<?> configurationClass ) {
		return configurationClass.getAnnotation( Administration.class ).value();
	}

	private Set<Pair<Class, Class>> transformToDslConfigurations( Set<BeanDefinition> configurationBeanDefinitions ) {
		final Set<Pair<Class, Class>> result = newLinkedHashSet();
		for ( BeanDefinition configurationBeanDefinition : configurationBeanDefinitions ) {
			final AnnotatedBeanDefinition annotatedBeanDefinition = ( AnnotatedBeanDefinition ) configurationBeanDefinition;
			final Class configurationClass = configurationClass( annotatedBeanDefinition );
			final Class domainType = configurationDomainType( configurationClass );

			result.add( Pair.create( domainType, configurationClass ) );
		}
		return result;
	}

	private Set<BeanDefinition> findAdministrationConfigurations( final String basePackage ) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false );
		provider.addIncludeFilter( new AnnotationTypeFilter( Administration.class ) );
		return provider.findCandidateComponents( basePackage );
	}

	private String scanBasePackage( final Element element ) {
		return element.getAttribute( BASE_PACKAGE_ATTRIBUTE );
	}

	private void registerValidatingRepositoryEventListener( final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( ValidatingRepositoryEventListener.class );

		Collection<BeanReference> validators = new ManagedSet<BeanReference>();
		validators.add( localValidatorFactoryBean( parserContext ) );

		Map<String, Collection<BeanReference>> eventValidators = new ManagedMap<String, Collection<BeanReference>>();
		eventValidators.put( "beforeSave", validators );

		builder.addPropertyValue( "validators", eventValidators );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		final String beanName = parserContext.getReaderContext().generateBeanName( beanDefinition );
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, beanName ) );
	}

	private BeanReference localValidatorFactoryBean( final ParserContext parserContext ) {
		RootBeanDefinition validatorDef = new RootBeanDefinition( LocalValidatorFactoryBean.class );
		String validatorName = parserContext.getReaderContext().registerWithGeneratedName( validatorDef );
		parserContext.registerComponent( new BeanComponentDefinition( validatorDef, validatorName ) );
		return new RuntimeBeanReference( validatorName );
	}

	private void loadSpringSecurityContext( final ParserContext parserContext ) {
		BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader( parserContext.getRegistry() );
		beanDefinitionReader.loadBeanDefinitions( SPRING_SECURITY_CONTEXT_RESOURCE );
	}

	private void registerRepositoryRestConfiguration( final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( RepositoryRestConfiguration.class );
		builder.addPropertyValue( "defaultPageSize", 5 );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, parserContext.getReaderContext().generateBeanName( beanDefinition ) ) );
	}

	private void registerViewPreparers( final ParserContext parserContext ) {
		registerSimpleBean( FOOTER_SECTION_VIEW_PREPARER_BEAN_NAME, FooterSectionViewPreparer.class, parserContext );
		registerSimpleBean( HEADER_SECTION_VIEW_PREPARER_BEAN_NAME, HeaderSectionViewPreparer.class, parserContext );
		registerSimpleBean( LEFT_SECTION_VIEW_PREPARER_BEAN_NAME, LeftSectionViewPreparer.class, parserContext );
		registerSimpleBean( LIST_VIEW_PREPARER_BEAN_NAME, ListViewPreparer.class, parserContext );
		registerSimpleBean( EDIT_VIEW_PREPARER_BEAN_NAME, EditViewPreparer.class, parserContext );
		registerSimpleBean( SHOW_VIEW_PREPARER_BEAN_NAME, ShowViewPreparer.class, parserContext );
		registerSimpleBean( SCREEN_VIEW_PREPARER_BEAN_NAME, ScreenViewPreparer.class, parserContext );
		registerSimpleBean( DASHBOARD_VIEW_PREPARER_BEAN_NAME, DashboardViewPreparer.class, parserContext );
	}

	private void registerGlobalAdministrationConfiguration( final ParserContext parserContext, final Map<Class<?>, BeanReference> domainTypeConfigurations ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( GlobalAdministrationConfiguration.class );
		builder.addPropertyValue( "domainTypeConfigurations", domainTypeConfigurations );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, GLOBAL_ADMINISTRATION_CONFIGURATION_BEAN_NAME ) );
	}

	private void registerRepositoryExporter( final ParserContext parserContext ) {
		registerSimpleBean( JPA_REPOSITORY_EXPORTER_BEAN_NAME, DynamicJpaRepositoryExporter.class, parserContext );
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

	private BeanReference registerSimpleBean( final Class<?> beanClass, final ParserContext parserContext ) {
		return new RuntimeBeanReference( parserContext.getReaderContext().registerWithGeneratedName( BeanDefinitionBuilder.rootBeanDefinition( beanClass ).getBeanDefinition() ) );
	}

	private BeanReference registerSimpleBean( final String beanName, final Class<?> beanClass, final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( beanClass );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, beanName ) );
		return new RuntimeBeanReference( beanName );
	}

	private Class<?> configurationClass( final AnnotatedBeanDefinition definition ) {
		final String configurationClassName = definition.getMetadata().getClassName();
		return ClassUtils.resolveClassName( configurationClassName, ClassUtils.getDefaultClassLoader() );
	}

	private String repositoryBeanName( final Class<?> domainType ) {
		return String.format( REPOSITORY_BEAN_NAME_FORMAT, uncapitalize( domainType.getSimpleName() ) );
	}

	private String domainTypeConfigurationBeanName( final Class<?> domainType ) {
		return String.format( DOMAIN_CONFIGURATION_BEAN_NAME_FORMAT, uncapitalize( domainType.getSimpleName() ) );
	}
}