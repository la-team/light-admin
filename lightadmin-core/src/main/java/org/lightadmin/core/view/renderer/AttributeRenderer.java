package org.lightadmin.core.view.renderer;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import javax.servlet.jsp.PageContext;
import java.io.IOException;

public interface AttributeRenderer {

	void render( final DomainTypeAttributeMetadata attributeMetadata, Object domainTypeObject, PageContext pageContext ) throws IOException;
}