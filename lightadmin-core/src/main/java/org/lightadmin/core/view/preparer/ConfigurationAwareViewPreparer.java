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
package org.lightadmin.core.view.preparer;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.web.ApplicationController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.lightadmin.core.util.NamingUtils.entityId;
import static org.lightadmin.core.util.NamingUtils.entityName;

public abstract class ConfigurationAwareViewPreparer implements ViewPreparer {

    private static final String GLOBAL_ADMINISTRATION_CONFIGURATION_KEY = "globalConfiguration";

    @Autowired
    protected GlobalAdministrationConfiguration globalAdministrationConfiguration;

    @Override
    public final void execute(Request request, AttributeContext attributeContext) {
        execute(request, attributeContext, globalAdministrationConfiguration);
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = domainTypeConfiguration(request);
        if (domainTypeAdministrationConfiguration != null) {
            execute(request, attributeContext, domainTypeAdministrationConfiguration);
        }
    }

    protected void execute(Request request, AttributeContext attributeContext, GlobalAdministrationConfiguration configuration) {
        addAttribute(attributeContext, GLOBAL_ADMINISTRATION_CONFIGURATION_KEY, globalAdministrationConfiguration, true);
    }

    protected void execute(Request request, AttributeContext attributeContext, DomainTypeAdministrationConfiguration configuration) {
        addAttribute(attributeContext, ApplicationController.DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY, configuration, true);

        addAttribute(attributeContext, "persistentEntity", configuration.getPersistentEntity(), true);
        addAttribute(attributeContext, "entityPluralName", configuration.getEntityConfiguration().getPluralName(), true);
        addAttribute(attributeContext, "entitySingularName", entitySingularName(request, configuration), true);

        addAttribute(attributeContext, "entity", entityFromRequest(request), true);
        addAttribute(attributeContext, "entityId", entityId(configuration, entityFromRequest(request)), true);
    }

    protected DomainTypeAdministrationConfiguration domainTypeConfiguration(final Request tilesContext) {
        return (DomainTypeAdministrationConfiguration) attributeFromRequest(tilesContext, ApplicationController.DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY);
    }

    protected Object attributeFromRequest(Request tilesContext, String attributeName) {
        ServletRequest servletTilesRequestContext = ServletUtil.getServletRequest(tilesContext);
        final HttpServletRequest httpServletRequest = servletTilesRequestContext.getRequest();

        return httpServletRequest.getAttribute(attributeName);
    }

    protected void addAttribute(AttributeContext attributeContext, String attributeName, Object attributeValue) {
        addAttribute(attributeContext, attributeName, attributeValue, false);
    }

    protected void addAttribute(AttributeContext attributeContext, String attributeName, Object attributeValue, boolean cascade) {
        attributeContext.putAttribute(attributeName, new Attribute(attributeValue), cascade);
    }

    private String entitySingularName(final Request request, final DomainTypeAdministrationConfiguration configuration) {
        final Object entity = entityFromRequest(request);
        if (entity == null) {
            return configuration.getEntityConfiguration().getSingularName();
        }
        return entityName(configuration, entity);
    }

    private Object entityFromRequest(Request request) {
        return attributeFromRequest(request, "entity");
    }
}