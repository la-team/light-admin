<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%@ taglib prefix="light" tagdir="/WEB-INF/tags" %>

<tiles:useAttribute name="listColumns"/>
<tiles:useAttribute name="domainTypeName"/>

<ul class="breadcrumb">
	<li><a href="<spring:url value="/"/>"><spring:message code="application.menu.dashboard"/></a></li><span class="divider">/</span>
	<li class="active">List ${domainTypeName}</li>
</ul>

<light:data-table entityName="${domainTypeName}" columns="${listColumns}"/>