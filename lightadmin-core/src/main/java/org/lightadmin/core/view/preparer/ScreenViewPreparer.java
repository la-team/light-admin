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

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.web.ApplicationController;
import org.springframework.beans.factory.annotation.Autowired;

import static org.lightadmin.core.util.NamingUtils.entityId;
import static org.lightadmin.core.util.NamingUtils.entityName;

public class ScreenViewPreparer extends ConfigurationAwareViewPreparer {

    private static final String GLOBAL_ADMINISTRATION_CONFIGURATION_KEY = "globalConfiguration";

    @Autowired
    private LightAdminConfiguration lightAdminConfiguration;

    @Override
    protected void execute(TilesRequestContext tilesContext, AttributeContext attributeContext, GlobalAdministrationConfiguration configuration) {
        super.execute(tilesContext, attributeContext, configuration);

        addAttribute(attributeContext, GLOBAL_ADMINISTRATION_CONFIGURATION_KEY, globalAdministrationConfiguration, true);
        addAttribute(attributeContext, "lightAdminConfiguration", lightAdminConfiguration, true);
    }

    @Override
    protected void execute(final TilesRequestContext tilesContext, final AttributeContext attributeContext, final DomainTypeAdministrationConfiguration configuration) {
        super.execute(tilesContext, attributeContext, configuration);

        addAttribute(attributeContext, "screenContext", configuration.getScreenContext(), true);

        addAttribute(attributeContext, ApplicationController.DOMAIN_TYPE_ADMINISTRATION_CONFIGURATION_KEY, configuration, true);

        addAttribute(attributeContext, "persistentEntity", configuration.getPersistentEntity(), true);
        addAttribute(attributeContext, "entityPluralName", configuration.getEntityConfiguration().getPluralName(), true);
        addAttribute(attributeContext, "entitySingularName", entitySingularName(tilesContext, configuration), true);

        addAttribute(attributeContext, "entity", entityFromRequest(tilesContext), true);
        addAttribute(attributeContext, "entityId", entityId(configuration, entityFromRequest(tilesContext)), true);
    }

    private String entitySingularName(final TilesRequestContext tilesContext, final DomainTypeAdministrationConfiguration configuration) {
        final Object entity = entityFromRequest(tilesContext);
        if (entity == null) {
            return configuration.getEntityConfiguration().getSingularName();
        }
        return entityName(configuration, entity);
    }

    private Object entityFromRequest(TilesRequestContext tilesContext) {
        return attributeFromRequest(tilesContext, "entity");
    }
}