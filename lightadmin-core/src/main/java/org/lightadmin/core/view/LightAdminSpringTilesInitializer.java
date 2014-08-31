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

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.el.ELAttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluator;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.evaluator.BasicAttributeEvaluatorFactory;
import org.apache.tiles.evaluator.impl.DirectAttributeEvaluator;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.tiles2.SpringLocaleResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;

import javax.servlet.jsp.JspFactory;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LightAdminSpringTilesInitializer extends AbstractTilesInitializer {

    private static final boolean tilesElPresent = ClassUtils.isPresent("org.apache.tiles.el.ELAttributeEvaluator", TilesConfigurer.class.getClassLoader());


    public static final String LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE = "org.apache.tiles.CONTAINER.LightAdmin";

    private String[] definitions;

    private Class<? extends PreparerFactory> preparerFactoryClass;

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context) {
        return new SpringTilesContainerFactory();
    }

    @Override
    protected String getContainerKey(TilesApplicationContext applicationContext) {
        return LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE;
    }

    private class SpringTilesContainerFactory extends BasicTilesContainerFactory {

        @Override
        protected BasicTilesContainer instantiateContainer(TilesApplicationContext context) {
            return new BasicTilesContainer();
        }

        @Override
        protected void registerRequestContextFactory(String className,
                                                     List<TilesRequestContextFactory> factories, TilesRequestContextFactory parent) {
            // Avoid Tiles 2.2 warn logging when default RequestContextFactory impl class not found
            if (ClassUtils.isPresent(className, TilesConfigurer.class.getClassLoader())) {
                super.registerRequestContextFactory(className, factories, parent);
            }
        }

        @Override
        protected List<URL> getSourceURLs(TilesApplicationContext applicationContext,
                                          TilesRequestContextFactory contextFactory) {
            if (definitions != null) {
                try {
                    List<URL> result = new LinkedList<>();
                    for (String definition : definitions) {
                        result.addAll(applicationContext.getResources(definition));
                    }
                    return result;
                } catch (IOException ex) {
                    throw new DefinitionsFactoryException("Cannot load definition URLs", ex);
                }
            } else {
                return super.getSourceURLs(applicationContext, contextFactory);
            }
        }

        @Override
        protected BaseLocaleUrlDefinitionDAO instantiateLocaleDefinitionDao(TilesApplicationContext applicationContext,
                                                                            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
            return super.instantiateLocaleDefinitionDao(applicationContext, contextFactory, resolver);
        }

        @Override
        protected DefinitionsReader createDefinitionsReader(TilesApplicationContext applicationContext,
                                                            TilesRequestContextFactory contextFactory) {
            DigesterDefinitionsReader reader = new DigesterDefinitionsReader();
            Map<String, String> map = new HashMap<>();
            map.put(DigesterDefinitionsReader.PARSER_VALIDATE_PARAMETER_NAME, Boolean.FALSE.toString());
            reader.init(map);
            return reader;
        }

        @Override
        protected DefinitionsFactory createDefinitionsFactory(TilesApplicationContext applicationContext,
                                                              TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
            return super.createDefinitionsFactory(applicationContext, contextFactory, resolver);
        }

        @Override
        protected PreparerFactory createPreparerFactory(TilesApplicationContext applicationContext,
                                                        TilesRequestContextFactory contextFactory) {
            if (preparerFactoryClass != null) {
                return BeanUtils.instantiate(preparerFactoryClass);
            } else {
                return super.createPreparerFactory(applicationContext, contextFactory);
            }
        }

        @Override
        protected LocaleResolver createLocaleResolver(TilesApplicationContext applicationContext,
                                                      TilesRequestContextFactory contextFactory) {
            return new SpringLocaleResolver();
        }

        @Override
        protected AttributeEvaluatorFactory createAttributeEvaluatorFactory(TilesApplicationContext applicationContext,
                                                                            TilesRequestContextFactory contextFactory, LocaleResolver resolver) {
            AttributeEvaluator evaluator;
            if (tilesElPresent && JspFactory.getDefaultFactory() != null) {
                evaluator = TilesElActivator.createEvaluator(applicationContext);
            } else {
                evaluator = new DirectAttributeEvaluator();
            }
            return new BasicAttributeEvaluatorFactory(evaluator);
        }
    }

    private static class TilesElActivator {

        public static AttributeEvaluator createEvaluator(TilesApplicationContext applicationContext) {
            ELAttributeEvaluator evaluator = new ELAttributeEvaluator();
            evaluator.setApplicationContext(applicationContext);
            evaluator.init(Collections.<String, String>emptyMap());
            return evaluator;
        }
    }

    public void setDefinitions(String[] definitions) {
        this.definitions = definitions;
    }

    public void setPreparerFactoryClass(Class<? extends PreparerFactory> preparerFactoryClass) {
        this.preparerFactoryClass = preparerFactoryClass;
    }
}