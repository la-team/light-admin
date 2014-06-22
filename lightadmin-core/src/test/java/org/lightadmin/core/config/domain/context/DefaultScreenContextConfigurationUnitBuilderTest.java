package org.lightadmin.core.config.domain.context;

import org.junit.Test;
import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;

import static org.junit.Assert.assertEquals;

public class DefaultScreenContextConfigurationUnitBuilderTest {

    @Test
    public void defaultConfigurationUnitCreatedForDomainType() throws Exception {
        ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().build();

        assertEquals(DomainConfigurationUnitType.SCREEN_CONTEXT, configurationUnit.getDomainConfigurationUnitType());
        assertEquals(DomainType.class, configurationUnit.getDomainType());
    }

    private DefaultScreenContextConfigurationUnitBuilder screenContextBuilder() {
        return new DefaultScreenContextConfigurationUnitBuilder(DomainType.class);
    }

    @Test
    public void defaultConfigurationUnitWithUndefinedScreenNameCreated() throws Exception {
        ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().build();

        assertEquals("DomainType", configurationUnit.getScreenName());
    }

    @Test
    public void configurationWithScreenNameDefined() throws Exception {
        ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().screenName("Test Screen Name").build();

        assertEquals("Test Screen Name", configurationUnit.getScreenName());
    }

    @Test
    public void configurationFullPacked() throws Exception {
        ScreenContextConfigurationUnit configurationUnit = screenContextBuilder().screenName("Test Screen Name").build();

        assertEquals("Test Screen Name", configurationUnit.getScreenName());
    }

    private static class DomainType {

    }
}