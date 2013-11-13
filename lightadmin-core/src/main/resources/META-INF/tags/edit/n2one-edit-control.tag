<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ attribute name="domainType" required="true" type="java.lang.Class" %>
<%@ attribute name="attributeMetadata" required="true" type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ attribute name="modalViewEnabled" required="false" type="java.lang.Boolean" %>

<tiles:useAttribute name="dialogMode" ignore="true"/>
<c:set var="dialogMode" value="${dialogMode eq null ? false : true}"/>

<div class="floatleft searchDrop">
    <select name="${attributeMetadata.name}" id="${attributeMetadata.name}${dialogMode ? '-dialog' : ''}" class="chzn-select" style="width: 302px;" ${disabled ? 'disabled' : ''} data-placeholder="Select ${attributeMetadata.name}">
        <option value=""></option>
        <light:domain-type-elements domainType="${attributeMetadata.type}" idVar="elementId" stringRepresentationVar="elementName">
            <option value="${elementId}"><c:out value="${elementName}" escapeXml="true"/></option>
        </light:domain-type-elements>
    </select>
</div>

<c:set var="domainTypeAdministrationConfiguration" value="${light:domainTypeAdministrationConfigurationFor(attributeMetadata.type)}"/>

<c:if test="${(domainType ne attributeMetadata.type) and (not dialogMode) and modalViewEnabled and (domainTypeAdministrationConfiguration ne null)}">
    <c:set var="domainTypeName" value="${light:cutLongText(domainTypeAdministrationConfiguration.domainTypeName)}"/>
    <light:url var="domainBaseUrl" value='${light:domainBaseUrl(domainTypeAdministrationConfiguration)}'/>

    <div class="floatleft" style="margin-left: 5px;">
        <a id="link-dialog-${attributeMetadata.name}" href="javascript:void(0);" title="Create ${domainTypeName}" class="btn14 mr5"><img src="<light:url value='/images/icons/dark/create.png'/>" alt="Create ${domainTypeName}"></a>
    </div>

    <script type="text/javascript">
        $(function () {
            modelFormViewDialog(
                    $("#link-dialog-${attributeMetadata.name}"),
                    '${domainTypeName}',
                    '${attributeMetadata.name}',
                    '${domainBaseUrl}'
            );
        });
    </script>
</c:if>