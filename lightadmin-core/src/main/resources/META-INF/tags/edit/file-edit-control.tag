<%@ tag body-content="empty" import="org.lightadmin.api.config.annotation.FileReference" %>
<%@ attribute name="attributeMetadata" required="true"
              type="org.springframework.data.mapping.PersistentProperty" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:importAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:importAttribute name="entityId" ignore="true"/>
<tiles:importAttribute name="dialogMode" ignore="true"/>

<c:set var="dialogMode" value="${dialogMode eq null ? false : true}"/>
<c:set var="fileReferenceConstraints" value='${light:findAnnotationByName(attributeMetadata, "org.lightadmin.api.config.annotation.FileReference$Constraints")}' />
<c:if test="${fileReferenceConstraints ne null}">
    <c:set var="extensions" value="${light:getAnnotationValue(fileReferenceConstraints, 'value')}" />
    <c:set var="limit" value="${light:getAnnotationValue(fileReferenceConstraints, 'limit')}" />
</c:if>
<div id="${attributeMetadata.name}-file-container${dialogMode ? '-dialog' : ''}" style="text-align: left;" data-extensions="${extensions ne null ? extensions : 'jpg,jpeg,png'}" data-limit="${limit ne null ? limit : '10'}">
    <div class="uploader" style="z-index: 1;">
        <input type="hidden" class="fileInput" id="${attributeMetadata.name}${dialogMode ? '-dialog' : ''}"
               name="${attributeMetadata.name}" size="24" style="opacity: 0;">
        <span class="filename">No file selected</span>
        <span class="action add"
              id="${attributeMetadata.name}-pickfiles${dialogMode ? '-dialog' : ''}">Choose File</span>
        <span style="display: none;" class="action remove"
              id="${attributeMetadata.name}-removefiles{dialogMode ? '-dialog' : ''}">Remove File</span>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        var resourceName = '${domainTypeAdministrationConfiguration.pluralDomainTypeName}';
        var attribute_name = '${attributeMetadata.name}';
        var entityId = '${entityId}';
        var file_upload_url = ApplicationConfig.getDomainEntityFilePropertyRestUrl(resourceName, $.isNumeric( entityId ) ? entityId : 0, attribute_name);

        var container = '${attributeMetadata.name}-file-container${dialogMode ? '-dialog' : ''}';
        var browse_button = '${attributeMetadata.name}-pickfiles${dialogMode ? '-dialog' : ''}';
        var file_input_id = '#${attributeMetadata.name}${dialogMode ? '-dialog' : ''}';
        var container_selector = '#' + container;

        var uploader = FileUploaderDecorator.decorate(container, file_input_id, attribute_name, browse_button, file_upload_url);
        $(container_selector).data('plupload', uploader);
    });
</script>