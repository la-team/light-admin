package org.lightadmin.core.view;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.dao.CachingLocaleUrlDefinitionDAO;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.locale.LocaleResolver;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.startup.BasicTilesInitializer;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.tiles2.SpringLocaleResolver;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LightAdminSpringTilesInitializer extends BasicTilesInitializer {

    public static final String LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE = "org.apache.tiles.CONTAINER.LightAdmin";

    private String[] definitions;
    private boolean checkRefresh;

    private Class<? extends PreparerFactory> preparerFactoryClass;

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext context) {
        return new SpringTilesContainerFactory();
    }

    public void setDefinitions(String[] definitions) {
        this.definitions = definitions;
    }

    public void setCheckRefresh(boolean checkRefresh) {
        this.checkRefresh = checkRefresh;
    }

    public void setPreparerFactoryClass(Class<? extends PreparerFactory> preparerFactoryClass) {
        this.preparerFactoryClass = preparerFactoryClass;
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
        protected void registerRequestContextFactory(String className, List<TilesRequestContextFactory> factories, TilesRequestContextFactory parent) {
            if (ClassUtils.isPresent(className, LightAdminSpringTilesInitializer.class.getClassLoader())) {
                super.registerRequestContextFactory(className, factories, parent);
            }
        }

        @Override
        protected List<URL> getSourceURLs(TilesApplicationContext applicationContext,
                                          TilesRequestContextFactory contextFactory) {
            if (definitions != null) {
                try {
                    List<URL> result = new LinkedList<URL>();
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
            BaseLocaleUrlDefinitionDAO dao = super.instantiateLocaleDefinitionDao(
                    applicationContext, contextFactory, resolver);
            if (checkRefresh && dao instanceof CachingLocaleUrlDefinitionDAO) {
                ((CachingLocaleUrlDefinitionDAO) dao).setCheckRefresh(checkRefresh);
            }
            return dao;
        }

        @Override
        protected DefinitionsReader createDefinitionsReader(TilesApplicationContext applicationContext,
                                                            TilesRequestContextFactory contextFactory) {
            DigesterDefinitionsReader reader = new DigesterDefinitionsReader();
            Map<String, String> map = new HashMap<String, String>();
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
    }
}