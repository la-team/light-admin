package org.lightadmin.core.view.renderer;

import java.io.IOException;
import java.io.Writer;

class IterableAttributeRenderer extends AbstractAttributeRenderer {

    private final AbstractAttributeRenderer attributeRenderer;

    public IterableAttributeRenderer(final AbstractAttributeRenderer attributeRenderer) {
        this.attributeRenderer = attributeRenderer;
    }

    @Override
    protected void write(final Object value, final Writer writer) throws IOException {
        final Iterable items = (Iterable) value;

        for (Object item : items) {
            attributeRenderer.write(item, writer);
            writer.write("<br/>");
        }
    }
}