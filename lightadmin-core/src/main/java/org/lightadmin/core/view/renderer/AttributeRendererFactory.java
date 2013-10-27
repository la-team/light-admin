package org.lightadmin.core.view.renderer;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

public interface AttributeRendererFactory {

    AttributeRenderer getRenderer(DomainTypeAttributeMetadata attributeMetadata);
}