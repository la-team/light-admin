<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light-jsp" uri="http://www.lightadmin.org/jsp" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<tiles:useAttribute name="fields"/>
<tiles:useAttribute name="scopes"/>
<tiles:useAttribute name="filters"/>
<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>
<c:set var="domainTypeEntityMetadata" value="${domainTypeAdministrationConfiguration.domainTypeEntityMetadata}"/>

<light:url var="domainRestScopeBaseUrl" value="${light:domainRestScopeBaseUrl(domainTypeName)}" scope="page"/>

<div class="title"><h5>List <c:out value="${domainTypeName}"/></h5></div>

<light-jsp:breadcrumb>
	<light-jsp:breadcrumb-item name="List ${domainTypeName}"/>
</light-jsp:breadcrumb>

<script type="text/javascript">
	var SEARCHER = createSearcher( '${domainRestScopeBaseUrl}' );
</script>

<light-jsp:search filters="${filters}"/>

<light-jsp:data-table domainTypeName="${domainTypeName}" fields="${fields}" scopes="${scopes}"
					  domainTypeEntityMetadata="${domainTypeEntityMetadata}"/>