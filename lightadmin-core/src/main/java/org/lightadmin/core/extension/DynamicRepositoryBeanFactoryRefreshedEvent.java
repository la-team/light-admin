package org.lightadmin.core.extension;

import org.springframework.context.ApplicationEvent;

public class DynamicRepositoryBeanFactoryRefreshedEvent extends ApplicationEvent {

    public DynamicRepositoryBeanFactoryRefreshedEvent(DynamicRepositoryBeanFactory source) {
        super(source);
    }

    public DynamicRepositoryBeanFactory getDynamicRepositoryBeanFactory() {
        return (DynamicRepositoryBeanFactory) super.getSource();
    }
}