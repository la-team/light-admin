package org.lightadmin.core.view.tags.form;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.rest.DomainTypeResourceSupport;
import org.lightadmin.core.view.tags.AbstractAutowiredTag;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.codehaus.jackson.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static org.codehaus.jackson.JsonGenerator.Feature.QUOTE_FIELD_NAMES;

public class DomainTypeMetadataJsonTag extends AbstractAutowiredTag {

	private static final JsonFactory JSON_FACTORY = new JsonFactory();

	static {
		JSON_FACTORY.configure( AUTO_CLOSE_TARGET, false );
		JSON_FACTORY.configure( QUOTE_FIELD_NAMES, false );
	}

	@Autowired
	private DomainTypeResourceSupport support;

	@Autowired
	private GlobalAdministrationConfiguration globalConfiguration;

	private DomainTypeEntityMetadata<DomainTypeAttributeMetadata> domainTypeMetadata;

	private Set<String> includedAttributes;

	@Override
	public void doTag() throws IOException {
		JsonGenerator json = JSON_FACTORY.createJsonGenerator( getJspContext().getOut() );
		json.writeStartObject();
		try {
			for ( DomainTypeAttributeMetadata attribMetadata : domainTypeMetadata.getAttributes() ) {
				if ( includedAttributes != null && !includedAttributes.contains( attribMetadata.getName() ) ) {
					continue;
				}
				try {
					json.writeObjectFieldStart( attribMetadata.getName() );
					json.writeStringField( "type", attribMetadata.getAttributeType().name() );
					if ( attribMetadata.isAssociation() ) {
						writeAssociationMetadata( attribMetadata, json );
					}
				} finally {
					json.writeEndObject();
				}
			}
		} finally {
			json.writeEndObject();
			json.close();
		}
	}

	private void writeAssociationMetadata( DomainTypeAttributeMetadata attribMetadata, JsonGenerator json ) throws IOException {
		Class<?> attribDomainType = attribMetadata.isCollectionLike() ? attribMetadata.getElementType() : attribMetadata.getType();
		DomainTypeBasicConfiguration attribDomainTypeConfig = globalConfiguration.forDomainType( attribDomainType );
		if ( attribDomainTypeConfig != null ) {
			DomainTypeAttributeMetadata idAttribMetadata = attribDomainTypeConfig.getDomainTypeEntityMetadata().getIdAttribute();
			json.writeStringField( "idAttribute", idAttribMetadata.getName() );
			String idPlaceholder = "{" + idAttribMetadata.getName() + "}";
			json.writeStringField( "hrefTemplate", support.selfLink( attribDomainTypeConfig, idPlaceholder ).getHref() );
		}
	}

	public void setDomainTypeMetadata( DomainTypeEntityMetadata<DomainTypeAttributeMetadata> domainTypeMetadata ) {
		this.domainTypeMetadata = domainTypeMetadata;
	}

	public void setIncludeFields( Collection<FieldMetadata> fields ) {
		includedAttributes = new HashSet<String>();
		for ( FieldMetadata field : fields ) {
			includedAttributes.add( field.getUuid() );
		}
	}

}
