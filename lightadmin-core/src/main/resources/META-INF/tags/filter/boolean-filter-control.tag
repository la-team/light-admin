<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
			  type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<input type="checkbox" id="${attributeMetadata.name}-yes" name="${attributeMetadata.name}" style="opacity: 0; "
	   value="true"/><label for="${attributeMetadata.name}-yes">Yes</label>
<input type="checkbox" id="${attributeMetadata.name}-no" name="${attributeMetadata.name}" style="opacity: 0; "
	   value="false"/><label for="${attributeMetadata.name}-no">No</label>