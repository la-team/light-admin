package org.springframework.data.rest.webmvc;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;
import org.springframework.data.rest.webmvc.support.BackendId;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.util.Set;

import static org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType.FORM_VIEW;

@RepositoryRestController
public class RepositoryDynamicEntityController extends AbstractRepositoryRestController {

    @Autowired
    public RepositoryDynamicEntityController(PagedResourcesAssembler<Object> pagedResourcesAssembler) {
        super(pagedResourcesAssembler);
    }

    @RequestMapping(value = "/{repository}/{id}/unit/{configurationUnit}", method = RequestMethod.GET)
    public ResponseEntity<?> entity(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, RootResourceInformation repoRequest, PersistentEntityResourceAssembler assembler, @PathVariable String repository, @BackendId Serializable id, @PathVariable String configurationUnit) throws Exception {
        DomainConfigurationUnitType configurationUnitType = DomainConfigurationUnitType.forName(configurationUnit);
        RepositoryInvoker invoker = repoRequest.getInvoker();

        Object domainObj = invoker.invokeFindOne(id);

        if (null == domainObj) {
            throw new ResourceNotFoundException();
        }

        Set<FieldMetadata> fields = domainTypeAdministrationConfiguration.fieldsForUnit(configurationUnitType);
        boolean exportBinaryData = configurationUnitType == FORM_VIEW;

        DynamicPersistentEntityResourceAssembler dynamicPersistentEntityResourceAssembler = DynamicPersistentEntityResourceAssembler.wrap(assembler, fields, exportBinaryData);

        DynamicPersistentEntityResource<Object> resource = dynamicPersistentEntityResourceAssembler.toResource(domainObj);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}