package org.lightadmin.core.config;

import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.config.support.AdministrationConfigBeanRegistryPostProcessor;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@SuppressWarnings( "unused" )
public class LighAdminWebApplicationInitializer implements WebApplicationInitializer {

	public static final String LIGHT_ADMINISTRATION_BASE_PACKAGE = "light:administration:base-package";
	public static final String LIGHT_ADMINISTRATION_BASE_URL = "light:administration:base-url";
	public static final String LIGHT_ADMIN_DISPATCHER_NAME = "lightadmin-dispatcher";

	@Override
	public void onStartup( final ServletContext servletContext ) throws ServletException {
		if ( lightAdminConfigurationNotEnabled( servletContext ) ) {
			servletContext.log( "No LightAdmin required configuration found." );
			return;
		}

		registerLightAdminDispatcher( servletContext );

		registerHiddenHttpMethodFilter( servletContext );

		registerSpringSecurityFilter( servletContext );

		registerCharsetFilter( servletContext );
	}

	private void registerLightAdminDispatcher( final ServletContext servletContext ) {
		final AnnotationConfigWebApplicationContext webApplicationContext = lightAdminApplicationContext( configurationsBasePackage( servletContext ) );

		final DispatcherServlet lightAdminDispatcher = new DispatcherServlet( webApplicationContext );

		ServletRegistration.Dynamic lightAdminDispatcherRegistration = servletContext.addServlet( LIGHT_ADMIN_DISPATCHER_NAME, lightAdminDispatcher );
		lightAdminDispatcherRegistration.setLoadOnStartup( 2 );
		lightAdminDispatcherRegistration.addMapping( lightAdminBaseUrl( servletContext ) );
	}

	private void registerHiddenHttpMethodFilter( final ServletContext servletContext ) {
		final String urlMapping = urlMapping( lightAdminBaseUrl( servletContext ) );

		servletContext.addFilter( "hiddenHttpMethodFilter", HiddenHttpMethodFilter.class ).addMappingForUrlPatterns( null, false, urlMapping );
	}

	private void registerSpringSecurityFilter( final ServletContext servletContext ) {
		final String urlMapping = urlMapping( lightAdminBaseUrl( servletContext ) );

		servletContext.addFilter( "springSecurityFilterChain", springSecurityFilterChain() ).addMappingForUrlPatterns( null, false, urlMapping );
	}

	private void registerCharsetFilter( final ServletContext servletContext ) {
		final String urlMapping = urlMapping( lightAdminBaseUrl( servletContext ) );

		servletContext.addFilter( "charsetFilter", characterEncodingFilter() ).addMappingForServletNames( null, false, urlMapping );
	}

	private AnnotationConfigWebApplicationContext lightAdminApplicationContext( final String configurationsBasePackage ) {
		AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
		webApplicationContext.register( LightAdminConfiguration.class );
		webApplicationContext.addBeanFactoryPostProcessor( new AdministrationConfigBeanRegistryPostProcessor( configurationsBasePackage ) );

		webApplicationContext.setDisplayName( "LightAdmin WebApplicationContext" );
		webApplicationContext.setNamespace( "lightadmin" );
		return webApplicationContext;
	}

	private DelegatingFilterProxy springSecurityFilterChain() {
		AnnotationConfigWebApplicationContext securityWebApplicationContext = new AnnotationConfigWebApplicationContext();
		securityWebApplicationContext.register( LightAdminSecurityConfiguration.class );
		securityWebApplicationContext.setNamespace( "lightadmin-security" );

		return new DelegatingFilterProxy( "springSecurityFilterChain", securityWebApplicationContext );
	}

	private CharacterEncodingFilter characterEncodingFilter() {
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding( "UTF-8" );
		characterEncodingFilter.setForceEncoding( true );
		return characterEncodingFilter;
	}

	private String urlMapping( String baseUrl ) {
		if ( StringUtils.endsWith( baseUrl, "/" ) ) {
			return baseUrl + "*";
		}
		return baseUrl + "/*";
	}

	private String configurationsBasePackage( final ServletContext servletContext ) {
		return servletContext.getInitParameter( LIGHT_ADMINISTRATION_BASE_PACKAGE );
	}

	private String lightAdminBaseUrl( final ServletContext servletContext ) {
		return servletContext.getInitParameter( LIGHT_ADMINISTRATION_BASE_URL );
	}

	private boolean lightAdminConfigurationNotEnabled( final ServletContext servletContext ) {
		return StringUtils.isBlank( lightAdminBaseUrl( servletContext ) ) || StringUtils.isBlank( configurationsBasePackage( servletContext ) );
	}
}