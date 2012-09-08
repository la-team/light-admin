package org.lightadmin.demo;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class AdministrationConfigBeanDefinitionParser implements BeanDefinitionParser {

	private static final String BASE_PACKAGE = "base-package";

	@Override
	public BeanDefinition parse( final Element element, final ParserContext parserContext ) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false );
		provider.addIncludeFilter( new AnnotationTypeFilter( Administration.class ) );

		final String basePackage = element.getAttribute( BASE_PACKAGE );

		for ( BeanDefinition definition : provider.findCandidateComponents( basePackage ) ) {
			Class<?> domainType = domainType( ( AnnotatedBeanDefinition ) definition );
			registerDomainRepository( domainType, parserContext );
		}

		registerRepositoryExporter( parserContext );

		return null;
	}

	private void registerRepositoryExporter( final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( DynamicJpaRepositoryExporter.class );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, "jpaRepositoryExporter" ) );
	}

	private void registerDomainRepository( final Class<?> domainType, final ParserContext parserContext ) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition( DynamicJpaRepositoryFactoryBean.class );
		builder.addPropertyValue( "repositoryInterface", DynamicJpaRepository.class );
		builder.addPropertyValue( "domainType", domainType );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		parserContext.registerBeanComponent( new BeanComponentDefinition( beanDefinition, repositoryName( domainType ) ) );
	}

	private String repositoryName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "Repository";
	}

	private Class<?> domainType( final AnnotatedBeanDefinition definition ) {
		return AnnotationAttributes.fromMap( definition.getMetadata().getAnnotationAttributes( Administration.class.getName(), false ) ).getClass( "value" );
	}
}