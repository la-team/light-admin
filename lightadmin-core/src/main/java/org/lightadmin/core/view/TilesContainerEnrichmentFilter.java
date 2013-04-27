package org.lightadmin.core.view;

import org.apache.tiles.servlet.context.ServletUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.tiles.startup.BasicTilesInitializer.CONTAINER_KEY_INIT_PARAMETER;

public class TilesContainerEnrichmentFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain ) throws ServletException, IOException {
		request.setAttribute( ServletUtil.CURRENT_CONTAINER_ATTRIBUTE_NAME, getServletContext().getAttribute( getContainerKey() ) );
		filterChain.doFilter( request, response );
	}

	private String getContainerKey() {
		return getServletContext().getInitParameter( CONTAINER_KEY_INIT_PARAMETER );
	}
}