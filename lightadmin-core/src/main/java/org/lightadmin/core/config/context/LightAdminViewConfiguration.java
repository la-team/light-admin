package org.lightadmin.core.config.context;

import org.lightadmin.core.view.preparer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LightAdminViewConfiguration {

    @Bean
    public FooterSectionViewPreparer footerSectionViewPreparer() {
        return new FooterSectionViewPreparer();
    }

    @Bean
    public HeaderSectionViewPreparer headerSectionViewPreparer() {
        return new HeaderSectionViewPreparer();
    }

    @Bean
    public LeftSectionViewPreparer leftSectionViewPreparer() {
        return new LeftSectionViewPreparer();
    }

    @Bean
    public ListViewPreparer listViewPreparer() {
        return new ListViewPreparer();
    }

    @Bean
    public FormViewPreparer formViewPreparer() {
        return new FormViewPreparer();
    }

    @Bean
    public ShowViewPreparer showViewPreparer() {
        return new ShowViewPreparer();
    }

    @Bean
    public DashboardViewPreparer dashboardViewPreparer() {
        return new DashboardViewPreparer();
    }

    @Bean
    public ScreenViewPreparer screenViewPreparer() {
        return new ScreenViewPreparer();
    }
}