<%@ tag import="org.lightadmin.core.config.domain.GlobalAdministrationConfiguration" %>
<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ attribute name="domainType" required="true" type="java.lang.Class" %>
<%@ attribute name="attributeMetadata" required="true" type="org.springframework.data.mapping.PersistentProperty" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ attribute name="modalViewEnabled" required="false" type="java.lang.Boolean" %>

<tiles:importAttribute name="dialogMode" ignore="true"/>

<tiles:importAttribute name="globalConfiguration"/>

<c:set var="dialogMode" value="${dialogMode eq null ? false : true}"/>
<jsp:useBean id="globalConfiguration" type="org.lightadmin.core.config.domain.GlobalAdministrationConfiguration"/>

<div class="floatleft searchDrop">
    <select name="${attributeMetadata.name}" id="${attributeMetadata.name}${dialogMode ? '-dialog' : ''}"
            class="chzn-select" style="width: 302px;" ${disabled ? 'disabled' : ''}
            data-placeholder="Select ${title}">
        <option value=""></option>
        <light:domain-type-elements domainType="${attributeMetadata.type}" idVar="elementId"
                                    stringRepresentationVar="elementName">
            <option value="${elementId}"><c:out value="${elementName}" escapeXml="true"/></option>
        </light:domain-type-elements>
    </select>
</div>

<c:if test="${(domainType ne attributeMetadata.actualType) and (not dialogMode) and modalViewEnabled}">
    <c:if test="<%= globalConfiguration.isManagedDomainType(attributeMetadata.getActualType())%>">

        <c:set var="domainTypeAdministrationConfiguration" value="<%= ((GlobalAdministrationConfiguration)globalConfiguration).forManagedDomainType(attributeMetadata.getActualType()) %>"/>
        <c:set var="domainTypeName" value="${light:cutLongText(domainTypeAdministrationConfiguration.domainTypeName)}"/>

        <div class="floatleft" style="margin-left: 5px;">
            <a id="link-dialog-${attributeMetadata.name}" href="javascript:void(0);" title="Create ${domainTypeName}"
               class="btn14 mr5"><img src="<light:url value='/images/icons/dark/create.png'/>" alt="Create ${domainTypeName}"></a>
        </div>

        <script type="text/javascript">
            $(function () {
                ModalDialogController.show(
                        '${domainTypeAdministrationConfiguration.pluralDomainTypeName}',
                        '${attributeMetadata.name}',
                        $("#link-dialog-${attributeMetadata.name}")
                );
            });
        </script>
    </c:if>
</c:if>