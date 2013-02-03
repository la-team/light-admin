package org.lightadmin.core.view.tags.form;

import java.io.IOException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import static org.codehaus.jackson.JsonGenerator.Feature.*;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.rest.DomainTypeResourceSupport;
import org.lightadmin.core.view.tags.AbstractAutowiredTag;

public class DomainTypeMetadataJsonTag extends AbstractAutowiredTag {

	private static final JsonFactory JSON_FACTORY = new JsonFactory();
	static {
		JSON_FACTORY.configure(AUTO_CLOSE_TARGET, false);
		JSON_FACTORY.configure(QUOTE_FIELD_NAMES, false);
	}

	@Autowired
	private DomainTypeResourceSupport support;

	@Autowired
	private GlobalAdministrationConfiguration globalConfiguration;

	private DomainTypeEntityMetadata<DomainTypeAttributeMetadata> domainTypeMetadata;

	@Override
	public void doTag() throws IOException {
		JsonGenerator json = JSON_FACTORY.createJsonGenerator(getJspContext().getOut());
		json.writeStartObject();
		try {
			for (DomainTypeAttributeMetadata attribMetadata : domainTypeMetadata.getAttributes()) {
				try {
					json.writeObjectFieldStart(attribMetadata.getName());
					json.writeStringField("type", DomainTypeAttributeType.by(attribMetadata).name());
					if (attribMetadata.isAssociation()) {
						writeAssociationMetadata(attribMetadata, json);
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

	private void writeAssociationMetadata(DomainTypeAttributeMetadata attribMetadata, JsonGenerator json) throws IOException {
		Class<?> attribDomainType = attribMetadata.isCollectionLike() ? attribMetadata.getElementType() : attribMetadata.getType();
		DomainTypeBasicConfiguration attribDomainTypeConfig = globalConfiguration.forDomainType(attribDomainType);
		if (attribDomainTypeConfig != null) {
			DomainTypeAttributeMetadata idAttribMetadata = attribDomainTypeConfig.getDomainTypeEntityMetadata().getIdAttribute();
			json.writeStringField("idAttribute", idAttribMetadata.getName());
			String idPlaceholder = "{" + idAttribMetadata.getName() + "}";
			json.writeStringField("hrefTemplate", support.selfLink(attribDomainTypeConfig, idPlaceholder).getHref());
		}
	}

	public void setDomainTypeMetadata(DomainTypeEntityMetadata<DomainTypeAttributeMetadata> domainTypeMetadata) {
		this.domainTypeMetadata = domainTypeMetadata;
	}

}
