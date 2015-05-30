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

import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.renderer.DefinitionRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.render.Renderer;
import org.apache.tiles.request.servlet.ServletUtil;
import org.springframework.web.servlet.view.tiles3.TilesView;

import static org.lightadmin.core.view.LightAdminSpringTilesInitializer.LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE;
import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

public class LightAdminTilesView extends TilesView {

    @Override
    public void afterPropertiesSet() throws Exception {
        ApplicationContext applicationContext = ServletUtil.getApplicationContext(getServletContext());
        setApplicationContext(applicationContext);

        if (getRenderer() == null) {
            TilesContainer container = TilesAccess.getContainer(applicationContext, LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE);
            Renderer renderer = new DefinitionRenderer(container);
            setRenderer(renderer);
        }
    }

    private void setApplicationContext(ApplicationContext applicationContext) {
        forDirectFieldAccess(this).setPropertyValue("applicationContext", applicationContext);
    }

    private Renderer getRenderer() {
        return (Renderer) forDirectFieldAccess(this).getPropertyValue("renderer");
    }
}