package org.lightadmin.core.view.renderer;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import java.io.IOException;
import java.io.Writer;

public class PersistentEntityAttributeRenderer extends AbstractAttributeRenderer {

    private final DomainTypeEntityMetadata entityMetadata;

    public PersistentEntityAttributeRenderer(final DomainTypeEntityMetadata entityMetadata) {
        this.entityMetadata = entityMetadata;
    }

    @Override
    protected void write(final Object attributeValue, final Writer writer) throws IOException {
        writer.write(String.format("%s #%s", entityMetadata.getEntityName(), entityMetadata.getIdAttribute().getValue(attributeValue)));
    }
}