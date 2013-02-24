<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
			type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<input id="${attributeMetadata.name}" name="${attributeMetadata.name}" ${disabled} type="text" size="10" readonly="true" class="input-date"/>
<label id="${attributeMetadata.name}-error" for="${attributeMetadata.name}" class="${errorCssClass}"></label>
