package org.lightadmin.core.config.support;

import com.google.common.collect.Collections2;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.DomainTypeAdministrationConfigPostProcessor;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.core.repository.support.DynamicJpaRepositoryFactoryBean;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.util.Pair;
import org.lightadmin.core.view.preparer.*;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class AdministrationConfigBeanDefinitionParser implements BeanDefinitionParser {

	private static final String SPRING_SECURITY_CONTEXT_RESOURCE = "classpath*:META-INF/spring/spring-security.xml";

	private static final String ENTITY_MANAGER_FACTORY_REF = "entity-manager-factory-ref";
	private static final String BASE_PACKAGE = "base-package";

	@Override
	public BeanDefinition parse( final Element element, final ParserContext parserContext ) {
		loadSpringSecurityContext( parserContext );

		// TODO: max: For the future!
		BeanReference entityManagerFactoryRef = entityManagerFactoryRef( element );

		final Set<BeanDefinition> administrationConfigurationsDefinitions = findAdministrationConfigurations( scanBasePackage( element ) );

		final Set<Pair<Class, Class>> dslConfigurations = transformToDslConfigurations( administrationConfigurationsDefinitions );

		registerDomainTypeRepositories( domainTypes( dslConfigurations ), parserContext );

		registerConfigurationBeans( dslConfigurations, parserContext );

		registerDomainConfigurationPostProcessor( parserContext );

		registerValidatingRepositoryEventListener( parserContext );

		registerRepositoryExporter( parserContext );

		registerRepositoryRestConfiguration( parserContext );

		registerViewPreparers( parserContext );

		return null;
	}

	private void registerDomainConfigurationPostProcessor( final ParserContext parserContext ) {
		registerSimpleBean( "domainTypeAdministrationConfigPostProcessor", DomainTypeAdministrationConfigPostProcessor.class, parserContext );
	}

	private BeanReference entityManagerFactoryRef( final Element element ) {
		if (element.hasAttribute( ENTITY_MANAGER_FACTORY_REF )) {
			return new RuntimeBeanReference(element.getAttribute("entity-manager-factory-ref"));
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
		return element.getAttribute( BASE_PACKAGE );
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
		registerSimpleBean( "footerSectionViewPreparer", FooterSectionViewPreparer.class, parserContext );
		registerSimpleBean( "headerSectionViewPreparer", HeaderSectionViewPreparer.class, parserContext );
		registerSimpleBean( "leftSectionViewPreparer", LeftSectionViewPreparer.class, parserContext );
		registerSimpleBean( "listViewPreparer", ListViewPreparer.class, parserContext );
		registerSimpleBean( "editViewPreparer", EditViewPreparer.class, parserContext );
		registerSimpleBean( "showViewPreparer", ShowViewPreparer.class, parserContext );
		registerSimpleBean( "screenViewPreparer", ScreenViewPreparer.class, parserContext );
		registerSimpleBean( "dashboardViewPreparer", DashboardViewPreparer.class, parserContext );
	}

	private void registerGlobalAdministrationConfiguration( final ParserContext parserContext, final Map<Class<?>, BeanReference> domainTypeConfigurations ) {
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

	private String repositoryBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "Repository";
	}

	private String domainTypeConfigurationBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "AdministrationConfiguration";
	}
}