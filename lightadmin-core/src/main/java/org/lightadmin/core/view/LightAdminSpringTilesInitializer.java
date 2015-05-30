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
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.el.ELAttributeEvaluator;
import org.apache.tiles.el.ScopeELResolver;
import org.apache.tiles.el.TilesContextBeanELResolver;
import org.apache.tiles.el.TilesContextELResolver;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.preparer.factory.PreparerFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.apache.tiles.startup.AbstractTilesInitializer;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.tiles3.SpringBeanPreparerFactory;

import javax.el.*;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspFactory;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class LightAdminSpringTilesInitializer extends AbstractTilesInitializer {

    private static final boolean tilesElPresent = ClassUtils.isPresent("org.apache.tiles.el.ELAttributeEvaluator", LightAdminSpringTilesInitializer.class.getClassLoader());

    public static final String LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE = "org.apache.tiles.CONTAINER.LightAdmin";

    private String[] definitions;

    private Class<? extends PreparerFactory> preparerFactoryClass;

    public LightAdminSpringTilesInitializer(String[] definitions) {
        this.definitions = definitions;
        this.preparerFactoryClass = SpringBeanPreparerFactory.class;
    }

    @Override
    protected String getContainerKey(ApplicationContext applicationContext) {
        return LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE;
    }

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(ApplicationContext context) {
        return new SpringTilesContainerFactory();
    }

    private class SpringTilesContainerFactory extends BasicTilesContainerFactory {

        @Override
        public TilesContainer createContainer(ApplicationContext context) {
            return super.createContainer(context);
        }

        @Override
        protected List<ApplicationResource> getSources(ApplicationContext applicationContext) {
            if (definitions != null) {
                List<ApplicationResource> result = new LinkedList<>();
                for (String definition : definitions) {
                    Collection<ApplicationResource> resources = applicationContext.getResources(definition);
                    if (resources != null) {
                        result.addAll(resources);
                    }
                }
                return result;
            } else {
                return super.getSources(applicationContext);
            }
        }

        @Override
        protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(ApplicationContext applicationContext,
                                                                            LocaleResolver resolver) {
            return super.instantiateLocaleDefinitionDao(applicationContext, resolver);
        }

        @Override
        protected DefinitionsReader createDefinitionsReader(ApplicationContext context) {
            return super.createDefinitionsReader(context);
        }

        @Override
        protected DefinitionsFactory createDefinitionsFactory(ApplicationContext applicationContext,
                                                              LocaleResolver resolver) {
            return super.createDefinitionsFactory(applicationContext, resolver);
        }

        @Override
        protected PreparerFactory createPreparerFactory(ApplicationContext context) {
            if (preparerFactoryClass != null) {
                return BeanUtils.instantiate(preparerFactoryClass);
            } else {
                return super.createPreparerFactory(context);
            }
        }

        @Override
        protected LocaleResolver createLocaleResolver(ApplicationContext context) {
            return new org.springframework.web.servlet.view.tiles3.SpringLocaleResolver();
        }

        @Override
        protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(ApplicationContext context,
                                                                            LocaleResolver resolver) {
            AttributeEvaluator evaluator;
            if (tilesElPresent && JspFactory.getDefaultFactory() != null) {
                ServletContext servletContext = (ServletContext) context.getContext();
                evaluator = new TilesElActivator().createEvaluator(servletContext);
            } else {
                evaluator = new DirectAttributeEvaluator();
            }
            return new BasicAttributeEvaluatorFactory(evaluator);
        }
    }

    private static class TilesElActivator {

        public AttributeEvaluator createEvaluator(ServletContext servletContext) {
            ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
            evaluator.setExpressionFactory(JspFactory.getDefaultFactory().getJspApplicationContext(servletContext).getExpressionFactory());
            evaluator.setResolver(new CompositeELResolverImpl());
            return evaluator;
        }
    }

    private static class CompositeELResolverImpl extends CompositeELResolver {

        public CompositeELResolverImpl() {
            add(new ScopeELResolver());
            add(new TilesContextELResolver(new TilesContextBeanELResolver()));
            add(new TilesContextBeanELResolver());
            add(new ArrayELResolver(false));
            add(new ListELResolver(false));
            add(new MapELResolver(false));
            add(new ResourceBundleELResolver());
            add(new BeanELResolver(false));
        }
    }
}