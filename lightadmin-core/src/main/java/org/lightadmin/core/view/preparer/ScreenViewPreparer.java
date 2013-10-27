package org.lightadmin.core.view.preparer;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class ScreenViewPreparer extends ConfigurationAwareViewPreparer {

    @Autowired
    private LightAdminConfiguration lightAdminConfiguration;

    @Override
    protected void execute(TilesRequestContext tilesContext, AttributeContext attributeContext, GlobalAdministrationConfiguration configuration) {
        super.execute(tilesContext, attributeContext, configuration);

        addAttribute(attributeContext, "lightAdminConfiguration", lightAdminConfiguration, true);
    }

    @Override
    protected void execute(final TilesRequestContext tilesContext, final AttributeContext attributeContext, final DomainTypeAdministrationConfiguration configuration) {
        super.execute(tilesContext, attributeContext, configuration);

        addAttribute(attributeContext, "screenContext", configuration.getScreenContext(), true);
    }
}