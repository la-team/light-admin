package org.lightadmin.core.extension;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.repository.support.Repositories;

public class RepositoriesRefreshedEvent extends ApplicationEvent {

    public RepositoriesRefreshedEvent(Repositories source) {
        super(source);
    }

    public Repositories getRepositories() {
        return (Repositories) super.getSource();
    }
}