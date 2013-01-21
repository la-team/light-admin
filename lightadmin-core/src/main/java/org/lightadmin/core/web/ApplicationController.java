package org.lightadmin.core.web;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings( {"unused", "unchecked"} )
@Controller
public class ApplicationController {

	private static final Logger LOG = LoggerFactory.getLogger( ApplicationController.class );

	public static final String BEAN_FACTORY_KEY = "beanFactory";
	public static final String ADMINISTRATION_CONFIGURATION_KEY = "administrationConfiguration";
	public static final String DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY = "domainTypeAdministrationConfiguration";

	@Autowired
	private GlobalAdministrationConfiguration configuration;

	@Autowired
	private ConfigurableApplicationContext appContext;

	@ResponseStatus( HttpStatus.BAD_REQUEST)
	@ExceptionHandler( Exception.class )
	public ModelAndView handleException( Exception ex ) {
		return new ModelAndView( "error-page" ).addObject( "exception", ex );
	}

	@ResponseStatus( HttpStatus.NOT_FOUND)
	@RequestMapping( value = "/page-not-found", method = RequestMethod.GET )
	public String handlePageNotFound() {
		return "page-not-found";
	}

	@ResponseStatus( HttpStatus.FORBIDDEN)
	@RequestMapping( value = "/access-denied", method = RequestMethod.GET )
	public String handleAccessDenied() {
		return "access-denied";
	}

	@RequestMapping( value = "/login", method = RequestMethod.GET )
	public String login() {
		return "login";
	}

	@RequestMapping( value = "/", method = RequestMethod.GET )
	public String root() {
		return "redirect:/dashboard";
	}

	@RequestMapping( value = "/dashboard", method = RequestMethod.GET )
	public String dashboard() {
		return "dashboard-view";
	}

	@RequestMapping( value = "/domain/{domainType}", method = RequestMethod.GET )
	public String list( @PathVariable String domainType, Model model ) {
		addDomainTypeConfigurationToModel( domainType, model );

		return "list-view";
	}

	@RequestMapping( value = "/domain/{domainTypeName}/{entityId}", method = RequestMethod.GET )
	public String show( @PathVariable String domainTypeName, @PathVariable long entityId, Model model ) {
		addDomainTypeConfigurationToModel( domainTypeName, model );

		model.addAttribute( "entity", repositoryForEntity( domainTypeName ).findOne( entityId ) );

		return "show-view";
	}

	@RequestMapping( value = "/domain/{domainTypeName}/{entityId}/edit", method = RequestMethod.GET )
	public String edit( @PathVariable String domainTypeName, @PathVariable long entityId, Model model ) {
		addDomainTypeConfigurationToModel( domainTypeName, model );

		model.addAttribute( "entity", repositoryForEntity( domainTypeName ).findOne( entityId ) );

		return "edit-view";
	}

	@RequestMapping( value = "/domain/{domainTypeName}/create", method = RequestMethod.GET )
	public String create( @PathVariable String domainTypeName, Model model ) {
		addDomainTypeConfigurationToModel( domainTypeName, model );

		return "create-view";
	}

	private void addDomainTypeConfigurationToModel( String domainTypeName, Model model ) {
		model.addAttribute( DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY, configuration.forEntityName( domainTypeName ) );
		model.addAttribute( BEAN_FACTORY_KEY, appContext.getAutowireCapableBeanFactory() );
	}

	private DynamicJpaRepository repositoryForEntity( final String domainType ) {
		return configuration.forEntityName( domainType ).getRepository();
	}
}
