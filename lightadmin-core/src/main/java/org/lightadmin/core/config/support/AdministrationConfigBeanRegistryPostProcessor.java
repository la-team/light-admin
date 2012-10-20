package org.lightadmin.core.config.support;

import com.google.common.collect.Collections2;
import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfigurationPostProcessor;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.support.DynamicJpaRepositoryFactoryBean;
import org.lightadmin.core.util.Pair;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@SuppressWarnings( "unused" )
public class AdministrationConfigBeanRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private org.springframework.beans.factory.support.BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	private final Set<Pair<Class, Class>> dslConfigurations = newLinkedHashSet();

	public AdministrationConfigBeanRegistryPostProcessor( final String configurationsBasePackage ) {
		dslConfigurations.addAll( transformToDslConfigurations( findAdministrationConfigurations( configurationsBasePackage ) ) );
	}

	public AdministrationConfigBeanRegistryPostProcessor( final Class... configurationClasses ) {
		for ( Class configurationClass : configurationClasses ) {
			dslConfigurations.add( Pair.create( configurationDomainType( configurationClass ), configurationClass ) );
		}
	}

	@Override
	public void postProcessBeanFactory( final ConfigurableListableBeanFactory beanFactory ) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry( final BeanDefinitionRegistry registry ) throws BeansException {
		registerDomainTypeRepositories( domainTypes( dslConfigurations ), registry );

		registerConfigurationBeans( dslConfigurations, registry );

		registerBeanPostProcessors( registry );
	}

	public Set<Pair<Class, Class>> getDslConfigurations() {
		return newLinkedHashSet( dslConfigurations );
	}

	private Set<BeanDefinition> findAdministrationConfigurations( final String basePackage ) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false );
		provider.addIncludeFilter( new AnnotationTypeFilter( Administration.class ) );
		return provider.findCandidateComponents( basePackage );
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

	private void registerConfigurationBeans( final Set<Pair<Class, Class>> dslConfigurations, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final Map<Class<?>, BeanReference> domainTypeConfigurations = registerDomainConfigurations( dslConfigurations, beanDefinitionRegistry );

		registerGlobalAdministrationConfiguration( beanDefinitionRegistry, domainTypeConfigurations );
	}

	private void registerBeanPostProcessors( final BeanDefinitionRegistry beanDefinitionRegistry ) {
		registerSimpleBean( GlobalAdministrationConfigurationPostProcessor.class, beanDefinitionRegistry );
	}

	private Collection<Class> domainTypes( final Set<Pair<Class, Class>> dynamicConfigurations ) {
		return Collections2.transform( dynamicConfigurations, Pair.<Class>extractFirstTransformer() );
	}

	private Class configurationClass( final AnnotatedBeanDefinition definition ) {
		final String configurationClassName = definition.getMetadata().getClassName();
		return ClassUtils.resolveClassName( configurationClassName, ClassUtils.getDefaultClassLoader() );
	}

	private Class configurationDomainType( final Class<?> configurationClass ) {
		return configurationClass.getAnnotation( Administration.class ).value();
	}

	private void registerDomainTypeRepositories( final Collection<Class> domainTypes, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		for ( Class domainType : domainTypes ) {
			registerDomainRepository( domainType, beanDefinitionRegistry );
		}
	}

	private String registerDomainRepository( final Class<?> domainType, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final String repositoryBeanName = BeanNameGenerator.INSTANCE.repositoryBeanName( domainType );

		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition( DynamicJpaRepositoryFactoryBean.class );
		builder.addConstructorArgValue( domainType );
		builder.addPropertyValue( "repositoryInterface", DynamicJpaRepository.class );

		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		beanDefinitionRegistry.registerBeanDefinition( repositoryBeanName, beanDefinition );

		return repositoryBeanName;
	}

	private void registerGlobalAdministrationConfiguration( final BeanDefinitionRegistry beanDefinitionRegistry, final Map<Class<?>, BeanReference> domainTypeConfigurations ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition( GlobalAdministrationConfiguration.class );
		builder.addPropertyValue( "domainTypeConfigurations", domainTypeConfigurations );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

		beanDefinitionRegistry.registerBeanDefinition( BeanNameGenerator.INSTANCE.globalAdministrationConfigurationBeanName(), beanDefinition );
	}

	private Map<Class<?>, BeanReference> registerDomainConfigurations( final Set<Pair<Class, Class>> dslConfigurations, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final Map<Class<?>, BeanReference> domainTypeConfigurations = new ManagedMap<Class<?>, BeanReference>();
		for ( Pair<Class, Class> configuration : dslConfigurations ) {
			final Class<?> domainType = configuration.first;
			final Class<?> configurationClass = configuration.second;

			final String beanName = BeanNameGenerator.INSTANCE.domainTypeConfigurationBeanName( domainType );

			final BeanReference beanReference = registerDomainConfigurationBean( beanName, configurationClass, beanDefinitionRegistry );

			domainTypeConfigurations.put( domainType, beanReference );
		}
		return domainTypeConfigurations;
	}

	private BeanReference registerDomainConfigurationBean( final String beanName, final Class<?> configurationClass, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final BeanDefinition domainConfigurationBeanDefinition = ConfigurationClassToBeanDefinitionTransformer.INSTANCE.apply( configurationClass );
		beanDefinitionRegistry.registerBeanDefinition( beanName, domainConfigurationBeanDefinition );
		return new RuntimeBeanReference( beanName );
	}

	private void registerSimpleBean( final Class<?> beanClass, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition( beanClass ).getBeanDefinition();
		final String beanName = generateBeanName( beanDefinition, beanDefinitionRegistry );

		beanDefinitionRegistry.registerBeanDefinition( beanName, beanDefinition );
	}

	private String generateBeanName( BeanDefinition beanDefinition, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		return beanNameGenerator.generateBeanName( beanDefinition, beanDefinitionRegistry );
	}
}