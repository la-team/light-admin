package org.lightadmin.core.web;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.ClassUtils.isAssignable;

@Controller
@SuppressWarnings({"unused", "unchecked"})
public class ApplicationController {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationController.class);

    public static final String BEAN_FACTORY_KEY = "beanFactory";
    public static final String ADMINISTRATION_CONFIGURATION_KEY = "administrationConfiguration";
    public static final String DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY = "domainTypeAdministrationConfiguration";

    @Autowired
    private GlobalAdministrationConfiguration configuration;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private LightAdminConfiguration lightAdminContext;

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        return new ModelAndView("error-page").addObject("exception", ex);
    }

    @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
    @RequestMapping(value = "/page-not-found", method = RequestMethod.GET)
    public String handlePageNotFound() {
        return "page-not-found";
    }

    @ResponseStatus(FORBIDDEN)
    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public String handleAccessDenied() {
        return "access-denied";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return redirectTo("/dashboard");
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "dashboard-view";
    }

    @RequestMapping(value = "/domain/{domainType}", method = RequestMethod.GET)
    public String list(@PathVariable String domainType, Model model) {
        addDomainTypeConfigurationToModel(domainType, model);

        return "list-view";
    }

    @RequestMapping(value = "/domain/{domainTypeName}/{entityId}", method = RequestMethod.GET)
    public String show(@PathVariable String domainTypeName, @PathVariable String entityId, Model model) {
        addDomainTypeConfigurationToModel(domainTypeName, model);

        final Object entity = findEntityOfDomain(entityId, domainTypeName);
        if (entity == null) {
            return pageNotFound();
        }

        model.addAttribute("entity", entity);
        return "show-view";
    }

    @RequestMapping(value = "/domain/{domainTypeName}/{entityId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String domainTypeName, @PathVariable String entityId, Model model) {
        addDomainTypeConfigurationToModel(domainTypeName, model);

        final Object entity = findEntityOfDomain(entityId, domainTypeName);
        if (entity == null) {
            return pageNotFound();
        }

        model.addAttribute("entity", entity);
        return "edit-view";
    }

    @RequestMapping(value = "/domain/{domainTypeName}/{entityId}/edit-dialog", method = RequestMethod.GET)
    public String editDialog(@PathVariable String domainTypeName, @PathVariable String entityId, Model model) {
        edit(domainTypeName, entityId, model);

        return "edit-dialog-view";
    }

    @RequestMapping(value = "/domain/{domainTypeName}/create", method = RequestMethod.GET)
    public String create(@PathVariable String domainTypeName, Model model) {
        addDomainTypeConfigurationToModel(domainTypeName, model);

        return "create-view";
    }

    @RequestMapping(value = "/domain/{domainTypeName}/create-dialog", method = RequestMethod.GET)
    public String createDialog(@PathVariable String domainTypeName, Model model) {
        create(domainTypeName, model);

        return "create-dialog-view";
    }

    private String pageNotFound() {
        return redirectTo("/page-not-found");
    }

    private Object findEntityOfDomain(String entityId, String domainTypeName) {
        final Serializable resolvedEntityId = resolveEntityId(entityId, configuration.forEntityName(domainTypeName).getDomainTypeEntityMetadata());

        return repositoryForEntity(domainTypeName).findOne(resolvedEntityId);
    }

    private void addDomainTypeConfigurationToModel(String domainTypeName, Model model) {
        model.addAttribute(DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY, configuration.forEntityName(domainTypeName));
        model.addAttribute(BEAN_FACTORY_KEY, appContext.getAutowireCapableBeanFactory());
    }

    private JpaRepository repositoryForEntity(final String domainType) {
        return configuration.forEntityName(domainType).getRepository();
    }

    private String redirectTo(final String url) {
        if ("/".equals(lightAdminContext.getApplicationBaseUrl())) {
            return "redirect:" + url;
        }

        return String.format("redirect:%s%s", lightAdminContext.getApplicationBaseUrl(), url);
    }

    private Serializable resolveEntityId(String entityId, DomainTypeEntityMetadata domainTypeEntityMetadata) {
        return stringToSerializable(entityId, (Class<? extends Serializable>) domainTypeEntityMetadata.getIdAttribute().getType());
    }

    private <V extends Serializable> V stringToSerializable(String s, Class<V> targetType) {
        if (isAssignable(targetType, String.class)) {
            return (V) s;
        }
        return conversionService.convert(s, targetType);
    }
}