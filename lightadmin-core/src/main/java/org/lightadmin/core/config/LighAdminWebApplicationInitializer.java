package org.lightadmin.core.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import static org.apache.commons.lang.BooleanUtils.toBoolean;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;

@SuppressWarnings("unused")
public class LighAdminWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup( final ServletContext servletContext ) throws ServletException {
		if ( lightAdminConfigurationNotEnabled( servletContext ) ) {
			servletContext.log( "LightAdmin Web Administration Module is disabled by default. Skipping." );
			return;
		}

		registerLightAdminDispatcher( servletContext );

		registerHiddenHttpMethodFilter( servletContext );

		if ( lightAdminSecurityEnabled( servletContext ) ) {
			registerSpringSecurityFilter( servletContext );
		}

		registerCharsetFilter( servletContext );
	}

	private void registerLightAdminDispatcher( final ServletContext servletContext ) {
		final AnnotationConfigWebApplicationContext webApplicationContext = lightAdminApplicationContext( servletContext );

		final DispatcherServlet lightAdminDispatcher = new DispatcherServlet( webApplicationContext );

		ServletRegistration.Dynamic lightAdminDispatcherRegistration = servletContext.addServlet( LIGHT_ADMIN_DISPATCHER_NAME, lightAdminDispatcher );
		lightAdminDispatcherRegistration.setLoadOnStartup( 2 );
		lightAdminDispatcherRegistration.addMapping( dispatcherUrlMapping( lightAdminBaseUrl( servletContext ) ) );
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

	private AnnotationConfigWebApplicationContext lightAdminApplicationContext( final ServletContext servletContext ) {
		AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();

		webApplicationContext.register( configurations( servletContext ) );

		webApplicationContext.setDisplayName( "LightAdmin WebApplicationContext" );
		webApplicationContext.setNamespace( "lightadmin" );
		return webApplicationContext;
	}

	private Class[] configurations( final ServletContext servletContext ) {
		if ( lightAdminSecurityEnabled( servletContext ) ) {
			return new Class[] {LightAdminContextConfiguration.class, LightAdminSecurityConfiguration.class};
		}
		return new Class[] {LightAdminContextConfiguration.class};
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

	private String dispatcherUrlMapping( String url ) {
		if ( "/".equals( url ) ) {
			return "/";
		}
		return urlMapping( url );
	}

	private String configurationsBasePackage( final ServletContext servletContext ) {
		return servletContext.getInitParameter( LIGHT_ADMINISTRATION_BASE_PACKAGE );
	}

	private String lightAdminBaseUrl( final ServletContext servletContext ) {
		return servletContext.getInitParameter( LIGHT_ADMINISTRATION_BASE_URL );
	}

	private boolean lightAdminSecurityEnabled( final ServletContext servletContext ) {
		return toBoolean( servletContext.getInitParameter( LIGHT_ADMINISTRATION_SECURITY ) );
	}

	private boolean lightAdminConfigurationNotEnabled( final ServletContext servletContext ) {
		return isBlank( lightAdminBaseUrl( servletContext ) ) || isBlank( configurationsBasePackage( servletContext ) );
	}
}