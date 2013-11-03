<%@ tag body-content="empty" %>
<%@ attribute name="attributeMetadata" required="true"
              type="org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>
<tiles:useAttribute name="entityId"/>
<tiles:useAttribute name="dialogMode" ignore="true"/>

<c:set var="dialogMode" value="${dialogMode eq null ? false : true}"/>

<light:url var="fileUploadUrl" value="/rest/upload" scope="page"/>
<light:url var="domainObjectUrl" value="${light:domainRestEntityBaseUrl(domainTypeAdministrationConfiguration, entityId)}" scope="page"/>

<c:set var="filePropertyUrl" value="${domainObjectUrl}/${attributeMetadata.name}/file" scope="page"/>

<div id="${attributeMetadata.name}-file-container${dialogMode ? '-dialog' : ''}" style="text-align: left;">
    <div class="uploader" style="z-index: 1;">
        <input type="hidden" class="fileInput" id="${attributeMetadata.name}${dialogMode ? '-dialog' : ''}" name="${attributeMetadata.name}" size="24" style="opacity: 0;">
        <span class="filename">No file selected</span>
        <span class="action add" id="${attributeMetadata.name}-pickfiles${dialogMode ? '-dialog' : ''}">Choose File</span>
        <span style="display: none;" class="action remove" id="${attributeMetadata.name}-removefiles{dialogMode ? '-dialog' : ''}">Remove File</span>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        var file_upload_url = '${fileUploadUrl}';
        var attribute_name = '${attributeMetadata.name}';

        var container = '${attributeMetadata.name}-file-container${dialogMode ? '-dialog' : ''}';
        var browse_button = '${attributeMetadata.name}-pickfiles${dialogMode ? '-dialog' : ''}';
        var file_input_id = '#${attributeMetadata.name}${dialogMode ? '-dialog' : ''}';

        decorateFileUploader(container, file_input_id, attribute_name, browse_button, file_upload_url);
    });
</script>