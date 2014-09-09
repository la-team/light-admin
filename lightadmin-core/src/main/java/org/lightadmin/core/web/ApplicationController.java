/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.web;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Controller
@SuppressWarnings({"unused", "unchecked"})
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    public static final String DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY = "domainTypeAdministrationConfiguration";
    public static final String BEAN_FACTORY_KEY = "beanFactory";

    @Autowired
    private GlobalAdministrationConfiguration configuration;

    @Autowired
    private ConfigurableApplicationContext appContext;

    @Autowired
    private LightAdminConfiguration lightAdminConfiguration;

    @Autowired
    @Qualifier("defaultConversionService")
    private ConversionService conversionService;

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
        DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forEntityName(domainTypeName);
        DynamicJpaRepository repository = domainTypeConfiguration.getRepository();

        PersistentEntity persistentEntity = domainTypeConfiguration.getPersistentEntity();
        Serializable id = (Serializable) conversionService.convert(entityId, persistentEntity.getIdProperty().getActualType());

        return repository.findOne(id);
    }

    private void addDomainTypeConfigurationToModel(String domainTypeName, Model model) {
        model.addAttribute(DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY, configuration.forEntityName(domainTypeName));
        model.addAttribute(BEAN_FACTORY_KEY, appContext.getAutowireCapableBeanFactory());
    }

    private String redirectTo(final String url) {
        return "redirect:" + absoluteUrlOf(applicationUrl(url));
    }

    private String absoluteUrlOf(String url) {
        return fromCurrentContextPath().path(url).build().toUriString();
    }

    private String applicationUrl(String value) {
        return lightAdminConfiguration.getApplicationUrl(value);
    }
}