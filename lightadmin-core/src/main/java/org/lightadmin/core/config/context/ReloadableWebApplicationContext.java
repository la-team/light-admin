package org.lightadmin.core.config.context;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ReloadableWebApplicationContext extends AnnotationConfigWebApplicationContext {

    private boolean reloadInProgress;

    public synchronized void reloadSingletons() {
        reloadInProgress = true;
        List<BeanFactoryPostProcessor> bfPostProcessors = new ArrayList<BeanFactoryPostProcessor>(getBeanFactoryPostProcessors());
        try {
            getBeanFactory().destroySingletons();
            getBeanFactoryPostProcessors().clear();
            super.refresh();
        } finally {
            getBeanFactoryPostProcessors().addAll(bfPostProcessors);
            reloadInProgress = false;
        }
    }

    @Override
    protected synchronized ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        return reloadInProgress ? getBeanFactory() : super.obtainFreshBeanFactory();
    }

}
