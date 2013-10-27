package org.lightadmin.core.view;

import org.apache.tiles.servlet.context.ServletUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.tiles.servlet.context.ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME;
import static org.lightadmin.core.view.LightAdminSpringTilesInitializer.LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE;

public class TilesContainerEnrichmentFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute(CURRENT_CONTAINER_ATTRIBUTE_NAME, ServletUtil.getContainer(getServletContext(), LIGHT_ADMIN_TILES_CONTAINER_ATTRIBUTE));
        filterChain.doFilter(request, response);
    }
}
