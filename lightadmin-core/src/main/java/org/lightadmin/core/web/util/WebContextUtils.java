package org.lightadmin.core.web.util;

import org.lightadmin.core.config.LighAdminWebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.ServletContext;

public abstract class WebContextUtils {

	public static WebApplicationContext getWebApplicationContext( ServletContext servletContext ) {
		return WebApplicationContextUtils.getWebApplicationContext( servletContext, servletContextAttributeName() );
	}

	private static String servletContextAttributeName() {
		return FrameworkServlet.SERVLET_CONTEXT_PREFIX + LighAdminWebApplicationInitializer.LIGHT_ADMIN_DISPATCHER_NAME;
	}
}