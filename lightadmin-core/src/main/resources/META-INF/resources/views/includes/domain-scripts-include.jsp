<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:importAttribute name="domainTypeAdministrationConfiguration"/>

<light:url var="baseUrl" value="/"/>
<light:url var="restBaseUrl" value="${light:restBaseUrl()}"/>

<script type="text/javascript">
    var ApplicationConfig = (function () {
        return {
            RESOURCE_NAME: '${domainTypeAdministrationConfiguration.pluralDomainTypeName}',

            BASE_URL: '${baseUrl}',
            REST_BASE_URL: '${restBaseUrl}',

            getDomainEntityCollectionUrl: function(resourceName) {
                return this.BASE_URL + 'domain/' + resourceName;
            },
            getDomainEntityCollectionRestUrl: function(resourceName) {
                return this.REST_BASE_URL + '/' + resourceName;
            },
            getDomainEntityMetadataRestUrl: function(resourceName) {
                return this.getDomainEntityCollectionRestUrl(resourceName) + '/metadata';
            },
            getDomainEntitySearchScopeRestUrl: function(resourceName) {
                return this.getDomainEntityCollectionRestUrl(resourceName) + '/scope';
            },
            getDomainEntityRestUrl: function(resourceName, entityId) {
                return this.getDomainEntityCollectionRestUrl(resourceName) + '/' + entityId;
            },
            getNewDomainEntityRestUrl: function(resourceName) {
                return this.getDomainEntityCollectionRestUrl(resourceName) + '/new';
            },
            getDomainEntityPropertyRestUrl: function(resourceName, entityId, propertyName) {
                return this.getDomainEntityRestUrl(resourceName, entityId) + '/' + propertyName;
            },
            getDomainEntityFilePropertyRestUrl: function(resourceName, entityId, propertyName) {
                return this.getDomainEntityPropertyRestUrl(resourceName, entityId, propertyName) + '/file';
            },
            getDomainEntityFilePropertyValueRestUrl: function(resourceName, entityId, propertyName) {
                return this.getDomainEntityPropertyRestUrl(resourceName, entityId, propertyName) + '/binary';
            },
            getDomainEntityUrl: function(resourceName, entityId) {
                return this.getDomainEntityCollectionUrl(resourceName) + '/' + entityId;
            },
            getEditDomainEntityUrl: function(resourceName, entityId) {
                return this.getDomainEntityUrl(resourceName, entityId) + '/edit';
            }
        };
    }());
</script>

<script type="text/javascript" src="<light:url value="/scripts/lightadmin-domain.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-service.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-search.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-renderer.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-serializer.js"/>"></script>

<script type="text/javascript" src="<light:url value="/scripts/decorators/lightadmin-datatables-ext.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/decorators/lightadmin-view-decoration.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/decorators/lightadmin-fileuploader-decorator.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/controllers/lightadmin-dialog-controller.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/controllers/lightadmin-notification-controller.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/controllers/lightadmin-formview-controller.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/controllers/lightadmin-quickview-controller.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/controllers/lightadmin-showview-controller.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/controllers/lightadmin-uploader-controller.js"/>"></script>