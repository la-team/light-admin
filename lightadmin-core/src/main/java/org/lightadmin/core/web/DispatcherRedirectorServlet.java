package org.lightadmin.core.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.lightadmin.core.util.LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_URL;

public class DispatcherRedirectorServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final ServletContext servletContext = getServletContext();

        final RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(redirectUrl(servletContext));
        if (requestDispatcher == null) {
            throw new IllegalStateException("A RequestDispatcher could not be located for " + LIGHT_ADMINISTRATION_BASE_URL);
        }
        requestDispatcher.forward(request, response);
    }

    private String redirectUrl(final ServletContext servletContext) {
        return servletContext.getInitParameter(LIGHT_ADMINISTRATION_BASE_URL) + "/";
    }
}