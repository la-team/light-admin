<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/breadcrumb" %>

<tiles:useAttribute name="fields"/>
<tiles:useAttribute name="scopes"/>
<tiles:useAttribute name="filters"/>
<tiles:useAttribute name="domainTypeAdministrationConfiguration"/>

<c:set var="domainTypeName" value="${domainTypeAdministrationConfiguration.domainTypeName}"/>
<c:set var="domainTypeEntityMetadata" value="${domainTypeAdministrationConfiguration.domainTypeEntityMetadata}"/>

<div class="title"><h5>List <c:out value="${domainTypeName}"/></h5></div>

<breadcrumb:breadcrumb>
	<breadcrumb:breadcrumb-item name="List ${domainTypeName}"/>
</breadcrumb:breadcrumb>

<light:search filters="${filters}" />

<light:data-table domainTypeName="${domainTypeName}" fields="${fields}" scopes="${scopes}" domainTypeEntityMetadata="${domainTypeEntityMetadata}" />