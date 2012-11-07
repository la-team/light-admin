package org.lightadmin.core.view.renderer;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfigurationAware;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.Date;

public class BasicAttributeRendererFactory implements AttributeRendererFactory, GlobalAdministrationConfigurationAware {

	private static final AttributeRenderer EMPTY_ATTRIBUTE_RENDERER = new EmptyAttributeRenderer();

	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Autowired
	private DomainTypeEntityMetadataResolver entityMetadataResolver;

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

		if ( isDomainTypeEntity( type ) ) {
			return domainTypeEntityAttributeRenderer( type );
		}

		if ( isPersistentEntity( type ) ) {
			return persistentEntityAttributeRenderer( type );
		}

		return createObjectAttributeRenderer();
	}

	private AbstractAttributeRenderer persistentEntityAttributeRenderer( final Class<?> type ) {
		final PersistentEntityAttributeRenderer persistentEntityAttributeRenderer = new PersistentEntityAttributeRenderer( entityMetadataResolver.resolveEntityMetadata( type ) );
		persistentEntityAttributeRenderer.setGlobalAdministrationConfiguration( globalAdministrationConfiguration );
		return persistentEntityAttributeRenderer;
	}

	private AbstractAttributeRenderer domainTypeEntityAttributeRenderer( final Class<?> type ) {
		final DomainTypeEntityAttributeRenderer domainTypeEntityAttributeRenderer = new DomainTypeEntityAttributeRenderer( domainTypeEntityMetadata( type ) );
		domainTypeEntityAttributeRenderer.setGlobalAdministrationConfiguration( globalAdministrationConfiguration );
		return domainTypeEntityAttributeRenderer;
	}

	private AbstractAttributeRenderer createObjectAttributeRenderer() {
		final ObjectAttributeRenderer objectAttributeRenderer = new ObjectAttributeRenderer();
		objectAttributeRenderer.setGlobalAdministrationConfiguration( globalAdministrationConfiguration );
		return objectAttributeRenderer;
	}

	private DomainTypeEntityMetadata domainTypeEntityMetadata( final Class<?> type ) {
		final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = globalAdministrationConfiguration.forDomainType( type );
		return domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
	}

	private boolean isPersistentEntity( final Class<?> type ) {
		return entityMetadataResolver.resolveEntityMetadata( type ) != null;
	}

	private boolean isDomainTypeEntity( final Class<?> type ) {
		return globalAdministrationConfiguration.forDomainType( type ) != null;
	}

	@Override
	@Autowired
	public void setGlobalAdministrationConfiguration( final GlobalAdministrationConfiguration configuration ) {
		this.globalAdministrationConfiguration = configuration;
	}

	private static class EmptyAttributeRenderer implements AttributeRenderer {

		@Override
		public void render( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject, final PageContext pageContext ) throws IOException {
			// nop
		}
	}
}