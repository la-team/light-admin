<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<input name="${attributeMetadata.name}" class="${cssClass}" ${disabled}></input>
<div id="${attributeMetadata.name}-error" class="${errorCssClass}"></div>
