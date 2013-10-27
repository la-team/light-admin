package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;

public class OperationBuilder {

    private GlobalAdministrationConfiguration configuration;
    private LightAdminConfiguration lightAdminConfiguration;

    private OperationBuilder(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        this.configuration = configuration;
        this.lightAdminConfiguration = lightAdminConfiguration;
    }

    public static OperationBuilder operationBuilder(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        return new OperationBuilder(configuration, lightAdminConfiguration);
    }

    public SaveFileRestOperation saveOperation(Object entity) {
        return new SaveFileRestOperation(configuration, lightAdminConfiguration, entity);
    }

    public DeleteFileRestOperation deleteOperation(Object entity) {
        return new DeleteFileRestOperation(configuration, lightAdminConfiguration, entity);
    }

    public GetFileRestOperation getOperation(Object entity) {
        return new GetFileRestOperation(configuration, lightAdminConfiguration, entity);
    }

    public FileExistsRestOperation fileExistsOperation(Object entity) {
        return new FileExistsRestOperation(configuration, lightAdminConfiguration, entity);
    }
}