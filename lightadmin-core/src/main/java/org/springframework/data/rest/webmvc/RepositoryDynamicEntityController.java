package org.springframework.data.rest.webmvc;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;
import org.springframework.data.rest.webmvc.support.BackendId;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.Set;

import static org.springframework.data.rest.webmvc.ControllerUtils.toResponseEntity;

@RepositoryRestController
public class RepositoryDynamicEntityController extends AbstractRepositoryRestController {

    private GlobalAdministrationConfiguration configuration;

    @Autowired
    public RepositoryDynamicEntityController(GlobalAdministrationConfiguration configuration, PagedResourcesAssembler<Object> pagedResourcesAssembler) {
        super(pagedResourcesAssembler);
        this.configuration = configuration;
    }

    @RequestMapping(value = "/{repository}/{id}/unit/{configurationUnit}", method = RequestMethod.GET)
    public ResponseEntity<?> entity(RootResourceInformation repoRequest, PersistentEntityResourceAssembler assembler, @PathVariable String repository, @BackendId Serializable id, @PathVariable String configurationUnit) throws Exception {
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forEntityName(repository);
        DomainConfigurationUnitType configurationUnitType = DomainConfigurationUnitType.forName(configurationUnit);
        RepositoryInvoker invoker = repoRequest.getInvoker();

        Object domainObj = invoker.invokeFindOne(id);

        if (null == domainObj) {
            throw new ResourceNotFoundException();
        }

        Set<FieldMetadata> fields = fields(domainTypeAdministrationConfiguration, configurationUnitType);

        return toResponseEntity(HttpStatus.OK, new HttpHeaders(), assembler.toResource(domainObj));

//        return negotiateResponse(request, HttpStatus.OK, new HttpHeaders(), new DomainTypeResource(entity, configurationUnitType, fields));
    }

    private Set<FieldMetadata> fields(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, final DomainConfigurationUnitType configurationUnitType) {
        switch (configurationUnitType) {
            case SHOW_VIEW:
                return domainTypeAdministrationConfiguration.getShowViewFragment().getFields();
            case FORM_VIEW:
                return domainTypeAdministrationConfiguration.getFormViewFragment().getFields();
            case QUICK_VIEW:
                return domainTypeAdministrationConfiguration.getQuickViewFragment().getFields();
            default:
                return domainTypeAdministrationConfiguration.getShowViewFragment().getFields();
        }
    }
}