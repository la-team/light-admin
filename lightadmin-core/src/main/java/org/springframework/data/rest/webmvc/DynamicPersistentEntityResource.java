package org.springframework.data.rest.webmvc;

import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.springframework.hateoas.Link;

import java.util.Set;

public class DynamicPersistentEntityResource<T> extends PersistentEntityResource<T> {

    private Set<FieldMetadata> fields;
    private boolean exportBinaryData;

    public static <T> DynamicPersistentEntityResource<T> wrap(PersistentEntityResource<T> persistentEntityResource, Set<FieldMetadata> fields, boolean exportBinaryData) {
        return new DynamicPersistentEntityResource<>(persistentEntityResource, fields, exportBinaryData);
    }

    private DynamicPersistentEntityResource(PersistentEntityResource<T> persistentEntityResource, Set<FieldMetadata> fields, boolean exportBinaryData) {
        super(persistentEntityResource.getPersistentEntity(), persistentEntityResource.getContent(), persistentEntityResource.getLinks().toArray(new Link[persistentEntityResource.getLinks().size()]));

        this.fields = fields;
        this.exportBinaryData = exportBinaryData;
    }

    public boolean isExportBinaryData() {
        return exportBinaryData;
    }

    public Set<FieldMetadata> getFields() {
        return fields;
    }
}