package org.lightadmin.core.view;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;
import org.apache.tiles.servlet.context.ServletUtil;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

import static org.lightadmin.core.view.LightAdminSpringTilesInitializer.LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE;

/**
 * {@link org.springframework.web.servlet.View} implementation that retrieves a
 * Tiles definition. The "url" property is interpreted as name of a Tiles definition.
 * <p/>
 * <p>This class builds on Tiles2, which requires JSP 2.0.
 * JSTL support is integrated out of the box due to JSTL's inclusion in JSP 2.0.
 * <b>Note: Spring 3.0 requires Tiles 2.1.2 or above.</b>
 * <p/>
 * <p>Depends on a TilesContainer which must be available in
 * the ServletContext. This container is typically set up via a
 * {@link org.springframework.web.servlet.view.tiles2.TilesConfigurer} bean definition in the application context.
 *
 * @author Juergen Hoeller
 * @see #setUrl
 * @see org.springframework.web.servlet.view.tiles2.TilesConfigurer
 * @since 2.5
 */
public class SeparateContainerTilesView extends AbstractUrlBasedView {

    private volatile boolean exposeForwardAttributes = false;


    /**
     * Checks whether we need to explicitly expose the Servlet 2.4 request attributes
     * by default.
     * <p>This will be done by default on Servlet containers up until 2.4, and skipped
     * for Servlet 2.5 and above. Note that Servlet containers at 2.4 level and above
     * should expose those attributes automatically! This feature exists for
     * Servlet 2.3 containers and misbehaving 2.4 containers only.
     */
    @Override
    protected void initServletContext(ServletContext sc) {
        if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
            this.exposeForwardAttributes = true;
        }
    }


    @Override
    public boolean checkResource(final Locale locale) throws Exception {
        TilesContainer container = ServletUtil.getContainer(getServletContext(), LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE);
        if (!(container instanceof BasicTilesContainer)) {
            // Cannot check properly - let's assume it's there.
            return true;
        }
        BasicTilesContainer basicContainer = (BasicTilesContainer) container;
        TilesApplicationContext appContext = new ServletTilesApplicationContext(getServletContext());
        TilesRequestContext requestContext = new ServletTilesRequestContext(appContext, null, null) {
            @Override
            public Locale getRequestLocale() {
                return locale;
            }
        };
        return (basicContainer.getDefinitionsFactory().getDefinition(getUrl(), requestContext) != null);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServletContext servletContext = getServletContext();
        TilesContainer container = ServletUtil.getContainer(servletContext, LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE);
        if (container == null) {
            throw new ServletException("Tiles container is not initialized. " + "Have you added a TilesConfigurer to your web application context?");
        }

        exposeModelAsRequestAttributes(model, request);
        JstlUtils.exposeLocalizationContext(new RequestContext(request, servletContext));

        if (!response.isCommitted()) {
            // Tiles is going to use a forward, but some web containers (e.g. OC4J 10.1.3)
            // do not properly expose the Servlet 2.4 forward request attributes... However,
            // must not do this on Servlet 2.5 or above, mainly for GlassFish compatibility.
            if (this.exposeForwardAttributes) {
                try {
                    WebUtils.exposeForwardRequestAttributes(request);
                } catch (Exception ex) {
                    // Servlet container rejected to set internal attributes, e.g. on TriFork.
                    this.exposeForwardAttributes = false;
                }
            }
        }

        container.render(getUrl(), request, response);
    }
}