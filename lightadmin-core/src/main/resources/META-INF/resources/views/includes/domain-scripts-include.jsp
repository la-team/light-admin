<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<light:url var="domainRestBaseUrl" value="${light:domainRestBaseUrl(domainTypeAdministrationConfiguration)}"/>
<light:url var="domainBaseUrl" value="${light:domainBaseUrl(domainTypeAdministrationConfiguration)}"/>

<script type="text/javascript">
    var ApplicationConfig = (function () {
        return {
            DOMAIN_ENTITY_BASE_REST_URL: '${domainRestBaseUrl}',
            DOMAIN_ENTITY_METADATA_REST_URL: '${domainRestBaseUrl}/metadata',
            DOMAIN_ENTITY_BASE_URL: '${domainBaseUrl}',

            getDomainEntityRestUrl: function(entityId) {
                return this.DOMAIN_ENTITY_BASE_REST_URL + '/' + entityId;
            },
            getDomainEntityUrl: function(entityId) {
                return this.DOMAIN_ENTITY_BASE_URL + '/' + entityId;
            },
            getEditDomainEntityUrl: function(entityId) {
                return this.DOMAIN_ENTITY_BASE_URL + '/' + entityId + '/edit';
            }
        };
    }());
</script>

<script type="text/javascript" src="<light:url value="/scripts/lightadmin-domain.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-service.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-search.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-renderer.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-datatables-ext.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-view-decoration.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin-fileuploader-decorator.js"/>"></script>
<script type="text/javascript" src="<light:url value="/scripts/lightadmin.js"/>"></script>