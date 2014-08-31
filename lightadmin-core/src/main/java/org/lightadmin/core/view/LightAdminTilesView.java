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
package org.lightadmin.core.view;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.tiles2.TilesView;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

import static org.lightadmin.core.view.LightAdminSpringTilesInitializer.LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE;

public class LightAdminTilesView extends TilesView {

    @Override
    public boolean checkResource(final Locale locale) throws Exception {
        TilesContainer container = ServletUtil.getContainer(getServletContext(), LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE);
        if (!(container instanceof BasicTilesContainer)) {
            // Cannot check properly - let's assume it's there.
            return true;
        }
        BasicTilesContainer basicContainer = (BasicTilesContainer) container;
        TilesApplicationContext appContext = new ServletTilesApplicationContext(getServletContext());
        TilesRequestContext requestContext = new ServletTilesRequestContext(appContext, null, null) {
            @Override
            public Locale getRequestLocale() {
                return locale;
            }
        };
        return (basicContainer.getDefinitionsFactory().getDefinition(getUrl(), requestContext) != null);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServletContext servletContext = getServletContext();
        TilesContainer container = ServletUtil.getContainer(getServletContext(), LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE);
        if (container == null) {
            throw new ServletException("Tiles container is not initialized. " +
                    "Have you added a TilesConfigurer to your web application context?");
        }

        exposeModelAsRequestAttributes(model, request);
        JstlUtils.exposeLocalizationContext(new RequestContext(request, servletContext));
        container.render(getUrl(), request, response);
    }
}