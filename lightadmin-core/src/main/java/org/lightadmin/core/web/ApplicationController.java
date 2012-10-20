package org.lightadmin.core.web;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ApplicationController {

	@SuppressWarnings( "unused" )
	private static final Logger LOG = LoggerFactory.getLogger( ApplicationController.class );

	public static final String DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY = "domainTypeAdministrationConfiguration";

	@Autowired
	private GlobalAdministrationConfiguration configuration;

	@SuppressWarnings( "unused" )
	@ExceptionHandler( Exception.class )
	public String handleException() {
		return "redirect:/";
	}

	@RequestMapping( value = "/", method = RequestMethod.GET )
	public String dashboard() {
		return "dashboardView";
	}

	@SuppressWarnings( "unchecked" )
	@RequestMapping( value = "/domain/{domainType}", method = RequestMethod.GET )
	public String list( @PathVariable String domainType, Model model ) {
		addDomainTypeConfigurationToModel( domainType, model );

		return "listView";
	}

	@SuppressWarnings( "unchecked" )
	@RequestMapping( value = "/domain/{domainTypeName}/{entityId}", method = RequestMethod.GET )
	public String show( @PathVariable String domainTypeName, @PathVariable long entityId, Model model ) {
		addDomainTypeConfigurationToModel( domainTypeName, model );

		model.addAttribute( "entity", repositoryForEntity( domainTypeName ).findOne( entityId ) );

		return "showView";
	}

	@SuppressWarnings( "unchecked" )
	@RequestMapping( value = "/domain/{domainTypeName}/{entityId}/edit", method = RequestMethod.GET )
	public String edit( @PathVariable String domainTypeName, @PathVariable long entityId, Model model ) {
		addDomainTypeConfigurationToModel( domainTypeName, model );

		model.addAttribute( "entity", repositoryForEntity( domainTypeName ).findOne( entityId ) );

		return "editView";
	}

	private void addDomainTypeConfigurationToModel( String domainTypeName, Model model ) {
		model.addAttribute( DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY, configuration.forEntityName( domainTypeName ) );
	}

	private DynamicJpaRepository repositoryForEntity( final String domainType ) {
		return configuration.forEntityName( domainType ).getRepository();
	}
}