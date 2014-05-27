package org.lightadmin.core.config.domain.configuration;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("unchecked")
public class DefaultEntityMetadataConfigurationUnitBuilderTest {

    @Test
    public void defaultConfigurationUnitCreatedForDomainType() throws Exception {
        final EntityMetadataConfigurationUnit configurationUnit = configurationBuilder().build();

        assertEquals(DomainConfigurationUnitType.CONFIGURATION, configurationUnit.getDomainConfigurationUnitType());
        assertEquals(DomainType.class, configurationUnit.getDomainType());
    }

    @Test
    public void configurationWithNameFieldExtractorCreated() throws Exception {
        final EntityMetadataConfigurationUnit configurationUnit = configurationBuilder()
                .nameField("name")
                .build();

        assertNotNull(configurationUnit.getNameExtractor());
        assertEquals("Domain Type Object Name", configurationUnit.getNameExtractor().apply(new DomainType()));
    }

    @Test
    public void configurationWithNameExtractorCreated() throws Exception {
        final EntityNameExtractor expectedEntityNameExtractor = EasyMock.createNiceMock(EntityNameExtractor.class);

        final EntityMetadataConfigurationUnit configurationUnit = configurationBuilder()
                .nameExtractor(expectedEntityNameExtractor)
                .build();

        assertNotNull(configurationUnit.getNameExtractor());
        assertEquals(expectedEntityNameExtractor, configurationUnit.getNameExtractor());
    }

    private DefaultEntityMetadataConfigurationUnitBuilder configurationBuilder() {
        return new DefaultEntityMetadataConfigurationUnitBuilder(DomainType.class);
    }

    private static class DomainType {

        private String name = "Domain Type Object Name";

        public String getName() {
            return name;
        }
    }
}