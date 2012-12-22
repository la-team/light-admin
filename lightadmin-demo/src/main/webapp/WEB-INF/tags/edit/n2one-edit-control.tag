<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ attribute name="attributeMetadata" required="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<select name="${attributeMetadata.name}" class="${cssClass}">
<light:domain-type-elements domainType="${attributeMetadata.type}" idVar="elementId" stringRepresentationVar="elementName">
	<option value="${elementId}"><c:out value="${elementName}" escapeXml="true"/></option>
</light:domain-type-elements>
</select>
<div id="${attributeMetadata.name}-error" class="${errorCssClass}"></div>
