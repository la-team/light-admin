<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ attribute name="attributeMetadata" required="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="errorCssClass" required="false" type="java.lang.String" %>
<div class="floatleft">
	<select name="${attributeMetadata.name}" id="${attributeMetadata.name}" style="opacity: 0; ">
		<light:domain-type-elements domainType="${attributeMetadata.type}" idVar="elementId" stringRepresentationVar="elementName">
			<option value="${elementId}"><c:out value="${elementName}" escapeXml="true"/></option>
		</light:domain-type-elements>
	</select>
	<label id="${attributeMetadata.name}-error" for="${attributeMetadata.name}" class="${errorCssClass}"></label>
</div>
