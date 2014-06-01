package org.springframework.data.rest.webmvc;

import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.support.Projector;
import org.springframework.hateoas.EntityLinks;

import java.util.Set;

import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

public class DynamicPersistentEntityResourceAssembler extends PersistentEntityResourceAssembler {

    private Set<FieldMetadata> fields;
    private boolean exportBinaryData;

    private DynamicPersistentEntityResourceAssembler(PersistentEntityResourceAssembler persistentEntityResourceAssembler, Set<FieldMetadata> fields, boolean exportBinaryData) {
        super(repositories(persistentEntityResourceAssembler), entityLinks(persistentEntityResourceAssembler), projector(persistentEntityResourceAssembler));

        this.fields = fields;
        this.exportBinaryData = exportBinaryData;
    }

    public static DynamicPersistentEntityResourceAssembler wrap(PersistentEntityResourceAssembler persistentEntityResourceAssembler, Set<FieldMetadata> fields, boolean exportBinaryData) {
        return new DynamicPersistentEntityResourceAssembler(persistentEntityResourceAssembler, fields, exportBinaryData);
    }

    @Override
    public DynamicPersistentEntityResource<Object> toResource(Object entity) {
        return DynamicPersistentEntityResource.wrap(super.toResource(entity), fields, exportBinaryData);
    }

    private static Repositories repositories(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (Repositories) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("repositories");
    }

    private static EntityLinks entityLinks(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (EntityLinks) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("entityLinks");
    }

    private static Projector projector(PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        return (Projector) forDirectFieldAccess(persistentEntityResourceAssembler).getPropertyValue("projector");
    }
}