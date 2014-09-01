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
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.web.ApplicationController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class ConfigurationAwareViewPreparer implements ViewPreparer {

    @Autowired
    protected GlobalAdministrationConfiguration globalAdministrationConfiguration;

    public final void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {
        execute(tilesContext, attributeContext, globalAdministrationConfiguration);
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = domainTypeConfiguration(tilesContext);
        if (domainTypeAdministrationConfiguration != null) {
            execute(tilesContext, attributeContext, domainTypeAdministrationConfiguration);
        }
    }

    protected void execute(TilesRequestContext tilesContext, AttributeContext attributeContext, GlobalAdministrationConfiguration configuration) {
    }

    protected void execute(TilesRequestContext tilesContext, AttributeContext attributeContext, DomainTypeAdministrationConfiguration configuration) {
    }

    protected DomainTypeAdministrationConfiguration domainTypeConfiguration(final TilesRequestContext tilesContext) {
        return (DomainTypeAdministrationConfiguration) attributeFromRequest(tilesContext, ApplicationController.DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY);
    }

    protected Object attributeFromRequest(TilesRequestContext tilesContext, String attributeName) {
        final ServletTilesRequestContext servletTilesRequestContext = ServletUtil.getServletRequest(tilesContext);
        final HttpServletRequest httpServletRequest = servletTilesRequestContext.getRequest();

        return httpServletRequest.getAttribute(attributeName);
    }

    protected void addAttribute(AttributeContext attributeContext, String attributeName, Object attributeValue) {
        addAttribute(attributeContext, attributeName, attributeValue, false);
    }

    protected void addAttribute(AttributeContext attributeContext, String attributeName, Object attributeValue, boolean cascade) {
        attributeContext.putAttribute(attributeName, new Attribute(attributeValue), cascade);
    }
}