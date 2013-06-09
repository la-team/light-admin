<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
			type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<input id="${attributeMetadata.name}" name="${attributeMetadata.name}" ${disabled ? 'disabled' : ''} type="text"/>
