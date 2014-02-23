package org.lightadmin.core.extension;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

public class RefreshableResourceMappingsTest {

    @Test
    public void testRefresh() {
        RefreshableResourceMappings resourceMappings = new RefreshableResourceMappings(config(), repositories());

        resourceMappings.refresh();
    }

    private Repositories repositories() {
        return new Repositories(new DefaultListableBeanFactory());
    }

    private RepositoryRestConfiguration config() {
        return new RepositoryRestConfiguration();
    }
}