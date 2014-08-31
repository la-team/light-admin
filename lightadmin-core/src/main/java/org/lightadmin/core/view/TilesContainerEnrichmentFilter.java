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
