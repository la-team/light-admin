package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;

public class OperationBuilder {

    private GlobalAdministrationConfiguration configuration;
    private WebContext webContext;

    private OperationBuilder(GlobalAdministrationConfiguration configuration, WebContext webContext) {
        this.configuration = configuration;
        this.webContext = webContext;
    }

    public static OperationBuilder operationBuilder(GlobalAdministrationConfiguration configuration, WebContext webContext) {
        return new OperationBuilder(configuration, webContext);
    }

    public SaveFileRestOperation saveOperation(Object entity) {
        return new SaveFileRestOperation(configuration, webContext, entity);
    }

    public DeleteFileRestOperation deleteOperation(Object entity) {
        return new DeleteFileRestOperation(configuration, webContext, entity);
    }

    public GetFileRestOperation getOperation(Object entity) {
        return new GetFileRestOperation(configuration, webContext, entity);
    }
}