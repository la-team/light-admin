package org.lightadmin.core.config;

import org.lightadmin.core.rest.DomainTypeToResourceConverter;
import org.lightadmin.core.web.ApplicationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.SpringBeanPreparerFactory;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;

import javax.persistence.EntityManagerFactory;

@Configuration
@Import( {
			 LightAdminDataConfiguration.class, LightAdminDomainConfiguration.class,
			 LightAdminRemoteConfiguration.class, LightAdminRepositoryRestConfiguration.class,
			 LightAdminSecurityConfiguration.class, LightAdminViewConfiguration.class
		 } )
@EnableWebMvc
public class LightAdminContextConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Override
	public void addInterceptors( InterceptorRegistry registry ) {
		registry.addWebRequestInterceptor( openEntityManagerInViewInterceptor() );
	}

	@Override
	public void addViewControllers( final ViewControllerRegistry registry ) {
		registry.addViewController( "/login" ).setViewName( "login" );
	}

	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry ) {
		registry.addResourceHandler( "/styles/**" ).addResourceLocations( "/styles/" ).setCachePeriod( 31556926 );
		registry.addResourceHandler( "/scripts/**" ).addResourceLocations( "/scripts/" );
		registry.addResourceHandler( "/images/**" ).addResourceLocations( "/images/" ).setCachePeriod( 31556926 );
	}

	@Bean
	public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
		OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
		openEntityManagerInViewInterceptor.setEntityManagerFactory( entityManagerFactory );
		return openEntityManagerInViewInterceptor;
	}

	@Override
	public void configureDefaultServletHandling( final DefaultServletHandlerConfigurer configurer ) {
		configurer.enable();
	}

	@Bean
	@Autowired
	public ConversionService conversionService( DomainTypeToResourceConverter domainTypeToResourceConverter ) {
		DefaultFormattingConversionService bean = new DefaultFormattingConversionService();
		bean.addConverter( domainTypeToResourceConverter );
		return bean;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename( "classpath:messages" );
		messageSource.setDefaultEncoding( "UTF-8" );
		messageSource.setCacheSeconds( 5 );
		return messageSource;
	}

	@Bean
	public ApplicationController applicationController() {
		return new ApplicationController();
	}

	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass( TilesView.class );
		return viewResolver;
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions( new String[] {
			"classpath*:META-INF/tiles/**/*.xml"
		} );
		configurer.setPreparerFactoryClass( SpringBeanPreparerFactory.class );
		configurer.setCheckRefresh( true );
		return configurer;
	}
}