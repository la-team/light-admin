package org.lightadmin.core.web;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@ExceptionHandler( Exception.class )
	public String handleException() {
		return "redirect:/";
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

	private void addDomainTypeConfigurationToModel( String domainTypeName, Model model ) {
		model.addAttribute( DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY, configuration.forEntityName( domainTypeName ) );
		model.addAttribute( BEAN_FACTORY_KEY, appContext.getAutowireCapableBeanFactory() );
	}

	private DynamicJpaRepository repositoryForEntity( final String domainType ) {
		return configuration.forEntityName( domainType ).getRepository();
	}
}
