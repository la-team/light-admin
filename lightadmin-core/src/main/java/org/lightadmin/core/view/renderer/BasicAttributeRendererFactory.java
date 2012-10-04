package org.lightadmin.core.view.renderer;

import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.GlobalAdministrationConfigurationAware;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Date;

public class BasicAttributeRendererFactory implements AttributeRendererFactory, GlobalAdministrationConfigurationAware {

	private static final AttributeRenderer EMPTY_ATTRIBUTE_RENDERER = new EmptyAttributeRenderer();

	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Override
	public AttributeRenderer getRenderer( final DomainTypeAttributeMetadata attributeMetadata ) {
		if ( attributeMetadata.isMapLike() ) {
			return EMPTY_ATTRIBUTE_RENDERER;
		}

		if ( attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike() ) {
			return new IterableAttributeRenderer( createAttributeRenderer( attributeMetadata.getElementType() ) );
		}

		return createAttributeRenderer( attributeMetadata.getType() );
	}

	private AbstractAttributeRenderer createAttributeRenderer( final Class<?> type ) {
		if ( Date.class.isAssignableFrom( type ) ) {
			return new DateAttributeRenderer();
		}

		if ( BeanUtils.isSimpleValueType( type ) ) {
			return new PrimitiveAttributeRenderer();
		}

		return createObjectAttributeRenderer();
	}

	private AbstractAttributeRenderer createObjectAttributeRenderer() {
		final ObjectAttributeRenderer objectAttributeRenderer = new ObjectAttributeRenderer();
		objectAttributeRenderer.setGlobalAdministrationConfigurationAware( globalAdministrationConfiguration );
		return objectAttributeRenderer;
	}

	@Override
	@Autowired
	public void setGlobalAdministrationConfigurationAware( final GlobalAdministrationConfiguration configuration ) {
		this.globalAdministrationConfiguration = configuration;
	}

	private static class EmptyAttributeRenderer implements AttributeRenderer {

		@Override
		public void render( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject, final PageContext pageContext ) throws IOException {
			// nop
		}
	}
}